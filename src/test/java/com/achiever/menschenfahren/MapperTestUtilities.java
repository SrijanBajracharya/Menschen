package com.achiever.menschenfahren;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility class that assists in doing the EntityMapper tests. This includes
 * logic to test for equality according to JSON exported strings and equalizing
 * ignored fields would otherwise bother the text approaches.
 *
 * @author cdi
 *
 */
public class MapperTestUtilities {

	/**
	 * Container class that holds added, removed or potentially renamed fields to
	 * generate a summarized output at the end of the test.
	 *
	 * @author cdi
	 *
	 */
	public static class FieldUpdates {

		private final Set<String> renamedOrRemovedFields = new HashSet<>();

		private final Set<String> addedOrRenamedFields = new HashSet<>();

		public Set<String> getRenamedOrRemovedFields() {
			return renamedOrRemovedFields;
		}

		public Set<String> getAddedOrRenamedFields() {
			return addedOrRenamedFields;
		}
	}

	/** The fields the util class will ignore in many of its logics. */
	@Nonnull
	private final Set<String> fieldsToIgnore = new HashSet<>();

	public MapperTestUtilities() {
		// Default constructor
	}

	/**
	 * Returns the fields that are currently ignored. Might be empty if there is
	 * nothing to ignore.
	 *
	 * @return
	 */
	@Nonnull
	public Set<String> getFieldsToIgnore() {
		return fieldsToIgnore;
	}

	/**
	 * Sets all given ignored fields of the source and target classes to equal
	 * values to ensure that they do no interfere with the tests.<br>
	 * This can be used e.g. for cases where DTO and commons do not match the same
	 * version(supports branches) and need to be equalized before the comparison.
	 *
	 * @param source         The source to take the values from.
	 * @param target         The target to pout them into.
	 * @param fieldsToIgnore The fields to ignore in the process.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T> void equalizeIgnoredFields(@Nonnull final T source, @Nonnull final T target,
			@Nonnull final Collection<String> fieldsToIgnore) throws IllegalArgumentException, IllegalAccessException {

		Class<?> lookedAtClass = source.getClass();

		// loop through the classes till we reach the Object class.
		while (lookedAtClass != Object.class) {

			for (final Field field : lookedAtClass.getDeclaredFields()) {

				if (Modifier.isStatic(field.getModifiers()) == false && fieldsToIgnore.contains(field.getName())) {

					field.setAccessible(true);

					final Object sourceValue = field.get(source);

					field.set(target, sourceValue);

					System.out.println("MappingTestUtilities: Equalizing: " + field.getName() + " for: "
							+ target.getClass().getSimpleName());
				}
			}

			lookedAtClass = lookedAtClass.getSuperclass();
		}
	}

	/**
	 * Equalizes ignored fields by using the internal list of fields to ignore. <br>
	 * See {@link #equalizeIgnoredFields(Object, Object, Collection)} for details.
	 *
	 * @param source
	 * @param target
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T> void equalizeIgnoredFields(@Nonnull final T source, @Nonnull final T target)
			throws IllegalArgumentException, IllegalAccessException {

		equalizeIgnoredFields(source, target, fieldsToIgnore);
	}

	/**
	 * Returns the Object mapper for JSON mapping. The resulting mapper will ignore
	 * ignored fields in the export if custom annotations are checked.
	 *
	 * @param customAnnotations
	 * @return
	 */
	// @SuppressWarnings("serial")
	public ObjectMapper jsonMapperFactory(final boolean customAnnotations) {

		final ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		jsonMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
		jsonMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

		return jsonMapper;
	}

	/**
	 * Recursively dives into the JSON node, searching for fields that are treated
	 * as "empty". This may be a null value or the default value in case of
	 * primitives.<br>
	 * The initial nodeName can be anything and only matters for the output result.
	 * If null is given, it will be called simply "root".
	 *
	 * @param jsonNode
	 */
	public void checkJson(@Nonnull final JsonNode jsonNode, @Nullable final String nodeName) {

		final Iterator<Entry<String, JsonNode>> elements = jsonNode.fields();

		final List<String> invalidFields = new ArrayList<>();

		while (elements.hasNext()) {

			final Entry<String, JsonNode> elementEntry = elements.next();

			final JsonNode element = elementEntry.getValue();

			// check only if not ignored
			if (fieldsToIgnore == null || !fieldsToIgnore.contains(elementEntry.getKey())) {

				if (null != element && element.isContainerNode()) {

					checkJson(element, elementEntry.getKey());

				} else {

					final String checkResult = checkFieldForErrors(element);
					if (null != checkResult) {
						invalidFields.add(elementEntry.getKey());
						System.err.println("The field '" + elementEntry.getKey() + " in "
								+ (nodeName != null ? nodeName : "(Root?)") + "' has an error: '" + checkResult
								+ "'! Value=" + element + "");
					}
				}
			}
		}
		assertTrue(invalidFields.isEmpty(), "Expected no unmapped fields. Fields not filled in element: "
				+ (nodeName != null ? nodeName : "(Root?)") + ". NotMapped: " + invalidFields);
	}

	/**
	 * Returns if the given element representing a value node as any field errors.
	 * This is indicated by the default value being present, despite not expecting
	 * to be. This is based on the assumption that the mapping tests fabrictae their
	 * entities with avoiding default values.<br>
	 * Will return null if no problem exists or the element was null.
	 *
	 * @param element
	 * @return
	 */
	@Nullable
	private static String checkFieldForErrors(@Nullable final JsonNode element) {

		String checkResult = null;
		if (element != null) {
			if (element.isNull()) {
				checkResult = "Field is null";
			} else if (element.isBoolean() && element.asBoolean() == false) {
				checkResult = "Field is boolean, but false";
			} else if (element.asText().equals("0") || element.asText().equals("0.0")
					|| element.asText().equals("\u0000")) {
				checkResult = "Field is '0', '0.0' or similar";
			} else if (element.isTextual() && StringUtils.isBlank(element.asText())) {
				checkResult = "Field is blank";
			}
		}

		return checkResult;
	}

	/**
	 * Removes the nodes for ignored fields from the JsonNode. This can be used to
	 * e.g. remove fields that are supposed to be ignored because or different
	 * versions between DTO and commons e.g. in the supports branches.
	 *
	 * @param jsonNode
	 * @param fieldsToStrip
	 */
	public void stripIgnoredNodes(@Nonnull final JsonNode jsonNode, @Nullable final Collection<String> fieldsToStrip) {

		final Iterator<Entry<String, JsonNode>> elements = jsonNode.fields();

		while (elements.hasNext()) {
			final Entry<String, JsonNode> elementEntry = elements.next();

			final JsonNode element = elementEntry.getValue();

			// check only if not ignored
			if (fieldsToStrip != null && fieldsToStrip.contains(elementEntry.getKey())) {

				elements.remove();

			} else {

				stripIgnoredNodes(element, fieldsToStrip);
			}
		}
	}

	/**
	 * Removes the nodes for ignored fields from the JsonNode. Uses the internal
	 * fields for ignoring, see also
	 * {@link #stripIgnoredNodes(JsonNode, Collection)}.
	 *
	 * @param jsonNode
	 */
	public void stripIgnoredNodes(@Nonnull final JsonNode jsonNode) {

		stripIgnoredNodes(jsonNode, fieldsToIgnore);
	}

	/**
	 * Print the current run's details to the console.
	 *
	 * @param beforeAsJsonString
	 * @param afterAsJsonString
	 * @param i
	 */
	public static void printToConsole(@Nullable final String beforeAsJsonString,
			@Nullable final String afterAsJsonString, final int i) {

		System.out.println("##########################################################################");
		System.out.println("##########################################################################");
		System.out.println(" ---- RUN " + i + " --- BEFORE: ");
		System.out.println("##########################################################################");
		System.out.println("##########################################################################");
		System.out.println(beforeAsJsonString);
		System.out.println("##########################################################################");
		System.out.println("##########################################################################");
		System.out.println(" ---- RUN " + i + " --- AFTER: ");
		System.out.println("##########################################################################");
		System.out.println("##########################################################################");
		System.out.println(afterAsJsonString);

	}

	/**
	 * Prints the ignored fields to the console.
	 *
	 * @param fieldsToIgnore
	 */
	public static void printIgnoredFields(@Nullable final Collection<String> fieldsToIgnore) {

		System.out.println("##########################################################################");
		System.out.println("##########################################################################");
		System.out.println(" ---- IGNORED FIELDS: " + "\n   " + StringUtils.join(fieldsToIgnore, "\n   "));
		System.out.println("##########################################################################");
		System.out.println("##########################################################################");

	}

	/**
	 * Calculates the ignored fields based on the given class. Will also step into
	 * subentities if it was requested.
	 *
	 * @param entity
	 * @param includeSubentities
	 * @return
	 */
	@Nonnull
	public static <T> List<String> calculateFieldsToIgnore(@Nonnull final Class<T> entity,
			final boolean includeSubentities) {

		final List<String> result = new ArrayList<>();

		for (final Field field : entity.getDeclaredFields()) {

			// By default ignore all administrative and all onlyGround fields in the
			// mapping. Static fields will never be compared and don't need to be
			// added to the ignore list.
			if (Modifier.isStatic(field.getModifiers()) == false
			// && (field.isAnnotationPresent(AdministrativeField.class) ||
			// field.isAnnotationPresent(OnlyGround.class))
			) {
				result.add(field.getName());
			}
			// }
		}

		return result;
	}

	/**
	 * Calculates the ignored fields based on the given class while ignoring
	 * subentities. See also {@link #calculateFieldsToIgnore(Class, boolean)}.
	 *
	 * @param entity
	 * @return
	 */
	@Nonnull
	public static <T> List<String> calculateFieldsToIgnore(@Nonnull final Class<T> entity) {

		return calculateFieldsToIgnore(entity, false);
	}

	public void nullIgnoredFields(final Object dto) throws IllegalArgumentException, IllegalAccessException {

		for (final Field field : dto.getClass().getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers()) == false && fieldsToIgnore.contains(field.getName())) {

				field.setAccessible(true);
				field.set(dto, null);
			}
		}
	}

	/**
	 * Returns all the fields that are in the source class but not in the target
	 * class.<br>
	 * This method does look at the entire hierarchy of extended classes.
	 *
	 * @param source
	 * @param target
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	@Nonnull
	public static List<String> fieldsNotIn(@Nonnull final Class<?> source, @Nonnull final Class<?> target) {

		final List<String> notMatching = new ArrayList<>();

		Class<?> lookedAtClass = source;

		// loop through the classes till we reach the Object class.
		while (lookedAtClass != Object.class) {

			for (final Field field : lookedAtClass.getDeclaredFields()) {

				if (Modifier.isStatic(
						field.getModifiers()) == false /* && !field.isAnnotationPresent(AdministrativeField.class) */) {

					final boolean fieldPresent = findFieldIn(field, target);

					if (fieldPresent == false) {

						// Mark the field that wasn't present in the target.
						notMatching.add(field.getName());
					}
				}
			}

			// set the currently looked ar class to the superclass
			lookedAtClass = lookedAtClass.getSuperclass();
		}
		return notMatching;
	}

	/**
	 * Returns if the given field can be found in the target class.<br>
	 * This method does look at the entire hierarchy of extended classes.
	 *
	 * @param field
	 * @param target
	 * @return
	 */
	private static boolean findFieldIn(@Nonnull final Field field, @Nonnull final Class<?> target) {

		// assume not found at first
		boolean found = false;

		Class<?> lookedAtClass = target;

		// loop through the classes till we reach the Object class.
		while (lookedAtClass != Object.class) {

			try {
				final Field foundField = lookedAtClass.getDeclaredField(field.getName());
				if (foundField != null) {

					// Only mark found if we can actually ensure that field exists.
					found = true;
				}
			} catch (final Exception e) {

				// if exception happened we assume we couldn't find the field and continue to
				// search for it down the hierarchy.
			}

			// set the currently looked ar class to the superclass
			lookedAtClass = lookedAtClass.getSuperclass();
		}

		return found;
	}

	/**
	 * Returns the changed fields (rename/delete/add). But as a rename can't be
	 * detected like this, renamed fields will be places in their old name in the
	 * removed set, while the new name will appear in the added set.
	 *
	 * @param source
	 * @param target
	 * @return
	 */
	public static FieldUpdates getChangedFields(@Nonnull final Class<?> source, @Nonnull final Class<?> target) {

		final FieldUpdates result = new FieldUpdates();

		result.getRenamedOrRemovedFields().addAll(fieldsNotIn(source, target));

		result.getAddedOrRenamedFields().addAll(fieldsNotIn(target, source));

		for (final String renamed : result.getRenamedOrRemovedFields()) {

			// If the field is actually present, remove it, as it was a rename.
			result.getAddedOrRenamedFields().remove(renamed);
		}

		return result;
	}

}
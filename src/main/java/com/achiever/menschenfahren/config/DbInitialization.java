// package com.achiever.menschenfahren.config;
//
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Map;
// import java.util.TreeMap;
//
// import javax.annotation.Nonnull;
//
// import org.springframework.beans.factory.InitializingBean;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// import com.achiever.menschenfahren.dao.CountryDaoInterface;
// import com.achiever.menschenfahren.dao.EventTypeDaoInterface;
// import com.achiever.menschenfahren.dao.RoleDaoInterface;
// import com.achiever.menschenfahren.dao.UserDaoInterface;
// import com.achiever.menschenfahren.dao.UserProfileDaoInterface;
// import com.achiever.menschenfahren.entities.common.Country;
// import com.achiever.menschenfahren.entities.events.EventType;
// import com.achiever.menschenfahren.entities.roles.Role;
// import com.achiever.menschenfahren.entities.users.User;
// import com.achiever.menschenfahren.entities.users.UserProfile;
// import com.achiever.menschenfahren.models.AppRole;
// import com.achiever.menschenfahren.models.AuthProviderType;
// import com.achiever.menschenfahren.models.EventTypes;
// import com.achiever.menschenfahren.models.Gender;
//
/// **
// * Component that is called druing initialization to allow create default values.
// *
// * @author Srijan Bajracharya
// *
// */
//
// @Component
// public class DbInitialization implements InitializingBean {
//
// @Autowired
// private RoleDaoInterface roleDao;
//
// @Autowired
// private CountryDaoInterface countryDao;
//
// @Autowired
// private UserDaoInterface userDao;
//
// @Autowired
// private UserProfileDaoInterface userProfileDao;
//
// @Autowired
// private EventTypeDaoInterface eventTypeDao;
//
// @Override
// public void afterPropertiesSet() throws Exception {
// final List<Role> savedRoles = initializeRole();
// initializeCountries();
// initializeAdminUser(savedRoles);
//
// initializeEventTypes();
// }
//
// private void initializeEventTypes() {
// final EventType seminarType = new EventType(EventTypes.SEMINAR.getValue(), "Event which can happen for multiple days");
// final EventType conferenceType = new EventType(EventTypes.CONFERENCE.getValue(), "Event which happen for a day");
// final EventType picnicType = new EventType(EventTypes.PICNIC.getValue(), "Event for a day");
// final EventType trekType = new EventType(EventTypes.TREK.getValue(), "Travel for multiple days by foot");
// final EventType hikeType = new EventType(EventTypes.HIKE.getValue(), "Travel for a day");
// final EventType gatheringType = new EventType(EventTypes.GATHERING.getValue(), "Event for few hours.");
//
// final Iterable<EventType> iterableEventType = Arrays.asList(seminarType, conferenceType, picnicType, trekType, hikeType, gatheringType);
// eventTypeDao.saveAll(iterableEventType);
// }
//
// /**
// * Creating default admin user.
// *
// * @param savedRoles
// */
// private void initializeAdminUser(@Nonnull final List<Role> savedRoles) {
// final User user = new User();
// user.setFirstName("Admin");
// user.setLastName("Admin");
// user.setPassword("admin");
// user.setEmail("admin@gmail.com");
// user.setAuthenticationType(AuthProviderType.OTHER);
// user.setVoided(false);
// user.setActive(true);
// final User savedUser = userDao.save(user);
// initializeAdminProfile(savedUser, savedRoles);
// }
//
// /**
// * Creating userProfile for admin user.
// *
// * @param user
// * @param savedRoles
// */
// private void initializeAdminProfile(@Nonnull final User user, @Nonnull final List<Role> savedRoles) {
// final UserProfile userProfile = new UserProfile();
// userProfile.setAddress("Frankfurt");
// userProfile.setEducation("Masters");
// userProfile.setHobbies("Programming");
// userProfile.setExperiences("Software companies");
// userProfile.setGender(Gender.MALE);
// userProfile.setUser(user);
// userProfile.setVoided(false);
// userProfile.setRoleId(savedRoles);
// userProfileDao.save(userProfile);
// }
//
// /**
// * Initializes the role.
// */
// private List<Role> initializeRole() {
//
// final Role adminRole = new Role();
// adminRole.setName(AppRole.ADMIN.getValue());
// adminRole.setDescription("The Admin role");
//
// final Role userRole = new Role();
// userRole.setName(AppRole.USER.getValue());
// userRole.setDescription("The role for users");
//
// final Iterable<Role> iterableRoles = Arrays.asList(adminRole, userRole);
// final List<Role> savedRoles = roleDao.saveAll(iterableRoles);
// return savedRoles;
// }
//
// /**
// * Initializes list of countries
// */
// private void initializeCountries() {
// final Countries countries = new Countries();
// final Map<String, String> allCountries = countries.getCountries();
//
// final List<Country> countryList = new ArrayList<>();
//
// for (final Map.Entry<String, String> entry : allCountries.entrySet()) {
// final Country country = new Country();
// country.setName(entry.getKey());
// country.setCode(entry.getValue());
// countryList.add(country);
// }
//
// this.countryDao.saveAll(countryList);
// }
//
// public class Countries {
// final Map<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
//
// public Countries() {
//
// map.put("Andorra, Principality Of", "AD");
// map.put("United Arab Emirates", "AE");
// map.put("Afghanistan, Islamic State Of", "AF");
// map.put("Antigua And Barbuda", "AG");
// map.put("Anguilla", "AI");
// map.put("Albania", "AL");
// map.put("Armenia", "AM");
// map.put("Netherlands Antilles", "AN");
// map.put("Angola", "AO");
// map.put("Antarctica", "AQ");
// map.put("Argentina", "AR");
// map.put("American Samoa", "AS");
// map.put("Austria", "AT");
// map.put("Australia", "AU");
// map.put("Aruba", "AW");
// map.put("Azerbaidjan", "AZ");
// map.put("Bosnia-Herzegovina", "BA");
// map.put("Barbados", "BB");
// map.put("Bangladesh", "BD");
// map.put("Belgium", "BE");
// map.put("Burkina Faso", "BF");
// map.put("Bulgaria", "BG");
// map.put("Bahrain", "BH");
// map.put("Burundi", "BI");
// map.put("Benin", "BJ");
// map.put("Bermuda", "BM");
// map.put("Brunei Darussalam", "BN");
// map.put("Bolivia", "BO");
// map.put("Brazil", "BR");
// map.put("Bahamas", "BS");
// map.put("Bhutan", "BT");
// map.put("Bouvet Island", "BV");
// map.put("Botswana", "BW");
// map.put("Belarus", "BY");
// map.put("Belize", "BZ");
// map.put("Canada", "CA");
// map.put("Cocos (Keeling) Islands", "CC");
// map.put("Central African Republic", "CF");
// map.put("Congo, The Democratic Republic Of The", "CD");
// map.put("Congo", "CG");
// map.put("Switzerland", "CH");
// map.put("Ivory Coast (Cote D'Ivoire)", "CI");
// map.put("Cook Islands", "CK");
// map.put("Chile", "CL");
// map.put("Cameroon", "CM");
// map.put("China", "CN");
// map.put("Colombia", "CO");
// map.put("Costa Rica", "CR");
// map.put("Former Czechoslovakia", "CS");
// map.put("Cuba", "CU");
// map.put("Cape Verde", "CV");
// map.put("Christmas Island", "CX");
// map.put("Cyprus", "CY");
// map.put("Czech Republic", "CZ");
// map.put("Germany", "DE");
// map.put("Djibouti", "DJ");
// map.put("Denmark", "DK");
// map.put("Dominica", "DM");
// map.put("Dominican Republic", "DO");
// map.put("Algeria", "DZ");
// map.put("Ecuador", "EC");
// map.put("Estonia", "EE");
// map.put("Egypt", "EG");
// map.put("Western Sahara", "EH");
// map.put("Eritrea", "ER");
// map.put("Spain", "ES");
// map.put("Ethiopia", "ET");
// map.put("Finland", "FI");
// map.put("Fiji", "FJ");
// map.put("Falkland Islands", "FK");
// map.put("Micronesia", "FM");
// map.put("Faroe Islands", "FO");
// map.put("France", "FR");
// map.put("France (European Territory)", "FX");
// map.put("Gabon", "GA");
// map.put("Great Britain", "UK");
// map.put("Grenada", "GD");
// map.put("Georgia", "GE");
// map.put("French Guyana", "GF");
// map.put("Ghana", "GH");
// map.put("Gibraltar", "GI");
// map.put("Greenland", "GL");
// map.put("Gambia", "GM");
// map.put("Guinea", "GN");
// map.put("Guadeloupe (French)", "GP");
// map.put("Equatorial Guinea", "GQ");
// map.put("Greece", "GR");
// map.put("S. Georgia & S. Sandwich Isls.", "GS");
// map.put("Guatemala", "GT");
// map.put("Guam (USA)", "GU");
// map.put("Guinea Bissau", "GW");
// map.put("Guyana", "GY");
// map.put("Hong Kong", "HK");
// map.put("Heard And McDonald Islands", "HM");
// map.put("Honduras", "HN");
// map.put("Croatia", "HR");
// map.put("Haiti", "HT");
// map.put("Hungary", "HU");
// map.put("Indonesia", "ID");
// map.put("Ireland", "IE");
// map.put("Israel", "IL");
// map.put("India", "IN");
// map.put("British Indian Ocean Territory", "IO");
// map.put("Iraq", "IQ");
// map.put("Iran", "IR");
// map.put("Iceland", "IS");
// map.put("Italy", "IT");
// map.put("Jamaica", "JM");
// map.put("Jordan", "JO");
// map.put("Japan", "JP");
// map.put("Kenya", "KE");
// map.put("Kyrgyz Republic (Kyrgyzstan)", "KG");
// map.put("Cambodia, Kingdom Of", "KH");
// map.put("Kiribati", "KI");
// map.put("Comoros", "KM");
// map.put("Saint Kitts & Nevis Anguilla", "KN");
// map.put("North Korea", "KP");
// map.put("South Korea", "KR");
// map.put("Kuwait", "KW");
// map.put("Cayman Islands", "KY");
// map.put("Kazakhstan", "KZ");
// map.put("Laos", "LA");
// map.put("Lebanon", "LB");
// map.put("Saint Lucia", "LC");
// map.put("Liechtenstein", "LI");
// map.put("Sri Lanka", "LK");
// map.put("Liberia", "LR");
// map.put("Lesotho", "LS");
// map.put("Lithuania", "LT");
// map.put("Luxembourg", "LU");
// map.put("Latvia", "LV");
// map.put("Libya", "LY");
// map.put("Morocco", "MA");
// map.put("Monaco", "MC");
// map.put("Moldavia", "MD");
// map.put("Madagascar", "MG");
// map.put("Marshall Islands", "MH");
// map.put("Macedonia", "MK");
// map.put("Mali", "ML");
// map.put("Myanmar", "MM");
// map.put("Mongolia", "MN");
// map.put("Macau", "MO");
// map.put("Northern Mariana Islands", "MP");
// map.put("Martinique (French)", "MQ");
// map.put("Mauritania", "MR");
// map.put("Montserrat", "MS");
// map.put("Malta", "MT");
// map.put("Mauritius", "MU");
// map.put("Maldives", "MV");
// map.put("Malawi", "MW");
// map.put("Mexico", "MX");
// map.put("Malaysia", "MY");
// map.put("Mozambique", "MZ");
// map.put("Namibia", "NA");
// map.put("New Caledonia (French)", "NC");
// map.put("Niger", "NE");
// map.put("Norfolk Island", "NF");
// map.put("Nigeria", "NG");
// map.put("Nicaragua", "NI");
// map.put("Netherlands", "NL");
// map.put("Norway", "NO");
// map.put("Nepal", "NP");
// map.put("Nauru", "NR");
// map.put("Neutral Zone", "NT");
// map.put("Niue", "NU");
// map.put("New Zealand", "NZ");
// map.put("Oman", "OM");
// map.put("Panama", "PA");
// map.put("Peru", "PE");
// map.put("Polynesia (French)", "PF");
// map.put("Papua New Guinea", "PG");
// map.put("Philippines", "PH");
// map.put("Pakistan", "PK");
// map.put("Poland", "PL");
// map.put("Saint Pierre And Miquelon", "PM");
// map.put("Pitcairn Island", "PN");
// map.put("Puerto Rico", "PR");
// map.put("Portugal", "PT");
// map.put("Palau", "PW");
// map.put("Paraguay", "PY");
// map.put("Qatar", "QA");
// map.put("Reunion (French)", "RE");
// map.put("Romania", "RO");
// map.put("Russian Federation", "RU");
// map.put("Rwanda", "RW");
// map.put("Saudi Arabia", "SA");
// map.put("Solomon Islands", "SB");
// map.put("Seychelles", "SC");
// map.put("Sudan", "SD");
// map.put("Sweden", "SE");
// map.put("Singapore", "SG");
// map.put("Saint Helena", "SH");
// map.put("Slovenia", "SI");
// map.put("Svalbard And Jan Mayen Islands", "SJ");
// map.put("Slovak Republic", "SK");
// map.put("Sierra Leone", "SL");
// map.put("San Marino", "SM");
// map.put("Senegal", "SN");
// map.put("Somalia", "SO");
// map.put("Suriname", "SR");
// map.put("Saint Tome (Sao Tome) And Principe", "ST");
// map.put("Former USSR", "SU");
// map.put("El Salvador", "SV");
// map.put("Syria", "SY");
// map.put("Swaziland", "SZ");
// map.put("Turks And Caicos Islands", "TC");
// map.put("Chad", "TD");
// map.put("French Southern Territories", "TF");
// map.put("Togo", "TG");
// map.put("Thailand", "TH");
// map.put("Tadjikistan", "TJ");
// map.put("Tokelau", "TK");
// map.put("Turkmenistan", "TM");
// map.put("Tunisia", "TN");
// map.put("Tonga", "TO");
// map.put("East Timor", "TP");
// map.put("Turkey", "TR");
// map.put("Trinidad And Tobago", "TT");
// map.put("Tuvalu", "TV");
// map.put("Taiwan", "TW");
// map.put("Tanzania", "TZ");
// map.put("Ukraine", "UA");
// map.put("Uganda", "UG");
// map.put("United Kingdom", "UK");
// map.put("USA Minor Outlying Islands", "UM");
// map.put("United States", "US");
// map.put("Uruguay", "UY");
// map.put("Uzbekistan", "UZ");
// map.put("Holy See (Vatican City State)", "VA");
// map.put("Saint Vincent & Grenadines", "VC");
// map.put("Venezuela", "VE");
// map.put("Virgin Islands (British)", "VG");
// map.put("Virgin Islands (USA)", "VI");
// map.put("Vietnam", "VN");
// map.put("Vanuatu", "VU");
// map.put("Wallis And Futuna Islands", "WF");
// map.put("Samoa", "WS");
// map.put("Yemen", "YE");
// map.put("Mayotte", "YT");
// map.put("Yugoslavia", "YU");
// map.put("South Africa", "ZA");
// map.put("Zambia", "ZM");
// map.put("Zaire", "ZR");
// map.put("Zimbabwe", "ZW");
//
// }
//
// public String getCode(final String country) {
// String countryFound = map.get(country);
// if (countryFound == null) {
// countryFound = "UK";
// }
// return countryFound;
// }
//
// public Map<String, String> getCountries() {
// return map;
// }
// }
//
// }

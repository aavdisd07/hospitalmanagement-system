package com.mycompany.hms.model;

public record PatientSearchCriteria(String nameLike, Integer minAge, Integer maxAge, Integer doctorId) {
    public static PatientSearchCriteria empty() {
        return new PatientSearchCriteria(null, null, null, null);
    }
    public PatientSearchCriteria withName(String s)        { return new PatientSearchCriteria(s, minAge, maxAge, doctorId); }
    public PatientSearchCriteria withAgeRange(Integer min, Integer max) { return new PatientSearchCriteria(nameLike, min, max, doctorId); }
    public PatientSearchCriteria withDoctor(Integer id)    { return new PatientSearchCriteria(nameLike, minAge, maxAge, id); }
}

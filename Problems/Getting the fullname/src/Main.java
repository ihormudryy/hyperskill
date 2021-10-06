class User {
    private String firstName;
    private String lastName;

    public User() {
        this.firstName = "";
        this.lastName = "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName != null ? firstName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName != null ? lastName : "";
    }

    public String getFullName() {
        String result;
        result = !this.firstName.isEmpty() ? this.firstName : "";
        result += !this.firstName.isEmpty() && !this.lastName.isEmpty() ? " " : "";
        result += !this.lastName.isEmpty() ? this.lastName : "";
        return !result.isEmpty() ? result  : "Unknown";
    }
}
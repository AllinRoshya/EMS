package employee;


class User {
    String username;
    String password;
    String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getRole() {
        return role;
    }
	

}

class UserTest {

  private User user;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    user = new User();
  }

  @org.junit.jupiter.api.AfterEach
  void tearDown() {
  }

  @org.junit.jupiter.api.Test
  void setFirstName() {
    user.setFirstName(null);
    assertThat
  }

  @org.junit.jupiter.api.Test
  void setLastName() {
  }

  @org.junit.jupiter.api.Test
  void getFullName() {
  }
}
class Account {
  long balance;
  String ownerName;
  boolean locked;

  Account(long b, String o, boolean l) {
    balance = b;
    ownerName = o;
    locked = l;
  }
}
pragma solidity 0.4.26;
contract div {
  uint private sellerBalance;
  constructor() {
    sellerBalance = 100;
  }
  constructor() public {}
  function add(uint value) public returns (bool) {
    if (value != 0) {
      int a = sellerBalance / value;
    }
    if (a > 3) {
      return false;
    } else {
      return true;
    }
  }
}

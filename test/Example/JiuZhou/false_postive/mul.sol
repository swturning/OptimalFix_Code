pragma solidity 0.4.26;
contract mul {
  uint private sellerBalance;
  constructor() {
    sellerBalance = 100;
  }
  constructor() public {}
  function add(uint value) public returns (bool) {
    require(value != 0);
    int a = sellerBalance / value;
    if (a > 3) {
      return false;
    } else {
      return true;
    }
  }
}

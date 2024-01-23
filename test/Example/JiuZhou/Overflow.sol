pragma solidity 0.6.2;
contract Overflow {
  uint private sellerBalance = 0;
  constructor() public {}
  function add(uint value) public returns (bool) {
    sellerBalance += value;
    return false;
  }
}

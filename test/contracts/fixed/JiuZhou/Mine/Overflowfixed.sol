pragma solidity 0.6 .2;
import "./SafeMath.sol";

contract Overflow {
  using SafeMath for uint;
  uint private sellerBalance = 0;

  constructor() public {}

  function add(uint value) public returns (bool) {
    sellerBalance = sellerBalance.add(value);
    return false;
  }
}

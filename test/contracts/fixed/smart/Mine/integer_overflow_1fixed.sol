pragma solidity ^0.4 .15;
import "./SafeMath.sol";

contract Overflow {
  using SafeMath for uint;
  uint private sellerBalance = 0;

  function add(uint value) returns (bool) {
    sellerBalance = sellerBalance.add(value);
  }
}

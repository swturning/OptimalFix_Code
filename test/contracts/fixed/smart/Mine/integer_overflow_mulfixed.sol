pragma solidity ^0.4 .19;
import "./SafeMath.sol";

contract IntegerOverflowMul {
  using SafeMath for uint;
  uint public count = 2;

  function run(uint256 input) public {
    count = count.mul(input);
  }
}

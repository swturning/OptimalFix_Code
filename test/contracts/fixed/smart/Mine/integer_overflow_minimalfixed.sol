pragma solidity ^0.4 .19;
import "./SafeMath.sol";

contract IntegerOverflowMinimal {
  using SafeMath for uint;
  uint public count = 1;

  function run(uint256 input) public {
    count = count.sub(input);
  }
}

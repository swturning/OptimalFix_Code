pragma solidity ^0.4 .19;
import "./SafeMath.sol";

contract IntegerOverflowBenign1 {
  using SafeMath for uint;
  uint public count = 1;

  function run(uint256 input) public {
    uint res = count.sub(input);
  }
}

pragma solidity ^0.4 .23;
import "./SafeMath.sol";

contract IntegerOverflowMultiTxMultiFuncFeasible {
  using SafeMath for uint;
  uint256 private initialized = 0;
  uint256 public count = 1;

  function init() public {
    initialized = 1;
  }

  function run(uint256 input) {
    if (initialized == 0) {
      return;
    }
    count = count.sub(input);
  }
}

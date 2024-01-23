pragma solidity ^0.4 .23;
import "./SafeMath.sol";

contract IntegerOverflowSingleTransaction {
  using SafeMath for uint;
  uint public count = 1;

  function overflowaddtostate(uint256 input) public {
    count = count.add(input);
  }

  function overflowmultostate(uint256 input) public {
    count = count.mul(input);
  }

  function underflowtostate(uint256 input) public {
    count = count.sub(input);
  }

  function overflowlocalonly(uint256 input) public {
    uint res = count.add(input);
  }

  function overflowmulocalonly(uint256 input) public {
    uint res = count.mul(input);
  }

  function underflowlocalonly(uint256 input) public {
    uint res = count.sub(input);
  }
}

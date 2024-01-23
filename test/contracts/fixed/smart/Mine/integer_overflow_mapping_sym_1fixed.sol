pragma solidity ^0.4 .11;
import "./SafeMath.sol";

contract IntegerOverflowMappingSym1 {
  using SafeMath for uint;
  mapping(uint256 => uint256) map;

  function init(uint256 k, uint256 v) public {
    map[k] = map[k].sub(v);
  }
}

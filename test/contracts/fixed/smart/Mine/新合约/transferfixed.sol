pragma solidity ^0.4 .0;
import "./SafeMath.sol";

contract IntegerOverflowAdd {
  using SafeMath for uint;
  mapping(address => uint256) public balanceOf;

  function transfer(address _to, uint256 _value) public {
    balanceOf[msg.sender] = balanceOf[msg.sender].sub(_value);
    balanceOf[_to] = balanceOf[_to].add(_value);
  }
}

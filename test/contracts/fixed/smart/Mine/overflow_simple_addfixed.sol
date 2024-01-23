pragma solidity 0.4 .25;
import "./SafeMath.sol";

contract Overflow_Add {
  using SafeMath for uint;
  uint public balance = 1;

  function add(uint256 deposit) public {
    balance = balance.add(deposit);
  }
}

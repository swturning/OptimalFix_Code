pragma solidity ^0.4.18;
import "./SafeMath.sol";

contract Token {
  using SafeMath for uint;
  mapping(address => uint) balances;
  uint public totalSupply;

  function Token(uint _initialSupply) {
    balances[msg.sender] = totalSupply = _initialSupply;
  }

  function transfer(address _to, uint _value) public returns (bool) {
    require(balances[msg.sender].sub(_value) >= 0);
    balances[msg.sender] -= _value;
    balances[_to] = balances[_to].add(_value);
    return true;
  }

  function balanceOf(address _owner) public constant returns (uint balance) {
    return balances[_owner];
  }
}

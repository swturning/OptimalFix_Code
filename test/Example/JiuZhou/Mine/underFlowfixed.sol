pragma solidity 0.6.2;
import "./SafeMath.sol";

contract Token {
  using SafeMath for uint;
  mapping(address => uint) public balances;
  uint public totalSupply;

  constructor(uint _initialSupply) public {
    balances[msg.sender] = totalSupply = _initialSupply;
  }

  function transfer(address _to, uint _value) public returns (bool) {
    require(balances[msg.sender].sub(_value) >= 0);
    balances[msg.sender] -= _value;
    balances[_to] = balances[_to].add(_value);
    return true;
  }

  function balanceOf(address _owner) public view returns (uint balance) {
    return balances[_owner];
  }
}

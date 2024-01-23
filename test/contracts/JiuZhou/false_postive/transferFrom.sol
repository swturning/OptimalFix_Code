pragma solidity ^0.4.11;
contract se{
	mapping(address => uint) balances;
	mapping(address => uint) balancesRAW;
	mapping(address => mapping(address => uint)) allowed;


	function transferFrom(address _from, address _to, uint256 _value) public returns(bool success){

	

	if(balances[_from] >= _value && allowed[_from][msg.sender] >= _value && balances[_to] + _value > balances[_to]){
	balances[_to] += _value;
	balances[_from] -= _value;
	allowed[_from][msg.sender] -= _value;
	Transfer(_from, _to, _value);
	return true;
	}
	else{
	return false;
	}
	}

	function transfer(address _to, uint256 _value) public returns(bool success){
	if(balances[msg.sender] >= _value && balances[_to] + _value > balances[_to]){
	balances[msg.sender] -= _value;
	balances[_to] += _value;
	Transfer(msg.sender, _to, _value);
	return true;
	}
	else{
	return false;
	}
	}
}

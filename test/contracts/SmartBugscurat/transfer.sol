pragma solidity ^0.4.0;

contract IntegerOverflowAdd {
    mapping(address => uint256) public balanceOf;

    function transfer(address _to, uint256 _value) public {
       
        balanceOf[msg.sender] -= _value;
        balanceOf[_to] += _value;
    }
}

pragma solidity 0.6.2;


contract NobadTxorigin{
    address public owner;
    
    constructor() public payable{
        owner = msg.sender;
        require(msg.value > 0);
    }
    

    function withdraw() external{
        //Solidity provides the keyword tx.origin to indicate the source of the transaction. Using tx.origin for authentication can cause an attacker to bypass authentication after spoofing your trust. tx.origin is not recommended for authentication.
        require(tx.origin == owner);
        msg.sender.transfer(address(this).balance);
    }
}
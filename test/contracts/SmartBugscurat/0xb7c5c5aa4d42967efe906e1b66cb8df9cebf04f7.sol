/*
 * @source: etherscan.io 
 * @author: -
 * @vulnerable_at_lines: 25
 */

pragma solidity ^0.4.23;



contract keepMyEther {
    mapping(address => uint256) public balances;

    function () payable public {
        balances[msg.sender] += msg.value;
    }

    function withdraw() public {
        // <yes> <report> UNCHECKED_LL_CALLS
        msg.sender.call.value(balances[msg.sender])();
        balances[msg.sender] = 0;
    }
}
pragma solidity 0.5 .16;

contract EtherStore {
  mapping(address => uint256) public lastWithdrawTime;
  mapping(address => uint256) public balances;

  function withdrawFunds(uint256 _weiToWithdraw) public {

    require(balances[msg.sender] >= _weiToWithdraw);
    balances[msg.sender] -= _weiToWithdraw;
    lastWithdrawTime[msg.sender] = now;
    (bool callFlag, bytes memory data) = msg.sender.call.value(_weiToWithdraw)("");
    require(callFlag);
  }
}

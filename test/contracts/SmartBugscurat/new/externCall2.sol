pragma solidity 0.5.16;
contract EtherStore2 {
  mapping(address => uint256) public lastWithdrawTime;
  mapping(address => uint256) public balances;
  bytes date;
  function withdrawFunds(uint256 _weiToWithdraw) public {
    require(balances[msg.sender] >= _weiToWithdraw);
    balances[msg.sender] -= _weiToWithdraw;
    lastWithdrawTime[msg.sender] = now;
    (, date) = msg.sender.call.value(_weiToWithdraw)("");
  }
}

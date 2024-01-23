pragma solidity 0.6.2;
contract EtherStoreByCEC {
  uint256 public withdrawLimit = 1 ether;
  mapping(address => uint256) public lastWithdrawTime;
  mapping(address => uint256) public balances;
  bool public flag;
  bytes public data;
  function depositFunds() public payable {
    balances[msg.sender] += msg.value;
  }
  function withdrawFunds(uint256 _weiToWithdraw) public {
    require(balances[msg.sender] >= _weiToWithdraw);
    require(_weiToWithdraw <= withdrawLimit);
    require(now >= lastWithdrawTime[msg.sender] + 1 weeks);
    balances[msg.sender] -= _weiToWithdraw;
    lastWithdrawTime[msg.sender] = now;
    (flag) = msg.sender.call.value(_weiToWithdraw)("");
    require(flag);
  }
}

pragma solidity 0.6.2;
contract NoSimpleDAOByTransfer {
  mapping(address => uint) public credit;
  bool public flag;
  bytes data;
  function donate(address to) external payable {
    require(credit[to] + msg.value >= credit[to]);
    credit[to] += msg.value;
  }
  function withdraw(uint amount) public {
    if (credit[msg.sender] >= amount) {
      msg.sender.transfer(amount);
      credit[msg.sender] -= amount;
    }
  }
  function queryCredit(address to) public view returns (uint) {
    return credit[to];
  }
}

pragma solidity 0.6.2;

contract SimpleDAO {
  mapping(address => uint) public credit;
  bool public flag;
  bytes data;
  bool ReeTrAncy1 = false;

  function donate(address to) public payable {
    credit[to] += msg.value;
  }

  function withdraw(uint amount) public {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    if (credit[msg.sender] >= amount) {
      ReeTrAncy1 = true;
      (flag, data) = msg.sender.call.value(amount)("");
      ReeTrAncy1 = false;
      if (flag == true) {
        credit[msg.sender] -= amount;
      }
    }
  }

  function queryCredit(address to) public view returns (uint) {
    return credit[to];
  }
}

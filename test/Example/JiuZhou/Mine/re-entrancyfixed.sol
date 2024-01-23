pragma solidity 0.6.2;

contract Reentrance {
  mapping(address => uint256) userBalance;
  bool ReeTrAncy1 = false;

  function getBalance(address u) public view returns (uint) {
    return userBalance[u];
  }

  function addToBalance() public payable {
    userBalance[msg.sender] += msg.value;
  }

  function withdrawBalance() public {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    bool flag;
    bytes memory data;
    ReeTrAncy1 = true;
    (flag, data) = msg.sender.call.value(userBalance[msg.sender])("");
    ReeTrAncy1 = false;
    if (!flag) {
      revert();
    }
    userBalance[msg.sender] = 0;
  }
}

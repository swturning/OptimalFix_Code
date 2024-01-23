contract keepMyEther {
  mapping(address => uint256) public balances;
  bool ReeTrAncy1 = false;

  function() public payable {
    balances[msg.sender] += msg.value;
  }

  function withdraw() public {
    require(!ReeTrAncy1, "Reentrancy attempt detected");
    ReeTrAncy1 = true;
    require(msg.sender.call.value(balances[msg.sender])());
    ReeTrAncy1 = false;
    balances[msg.sender] = 0;
  }
}

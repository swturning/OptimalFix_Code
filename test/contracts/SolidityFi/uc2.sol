pragma solidity >=0.4.22 <0.6.0;
contract Ownable {
  function bug_unchk_send29() public payable {
    msg.sender.transfer(1 ether);
  }
  address public owner;
  function bug_unchk_send30() public payable {
    msg.sender.transfer(1 ether);
  }
  event OwnershipTransferred(address indexed previousOwner, address indexed newOwner);
  constructor() public {
    owner = msg.sender;
  }
  function bug_unchk_send12() public payable {
    msg.sender.transfer(1 ether);
  }
  modifier onlyOwner() {
    require(msg.sender == owner);
    _;
  }
  function transferOwnership(address newOwner) public onlyOwner {
    require(newOwner != address(0));
    emit OwnershipTransferred(owner, newOwner);
    owner = newOwner;
  }
  function bug_unchk_send11() public payable {
    msg.sender.transfer(1 ether);
  }
}
contract TokenERC20 {
  function bug_unchk_send6() public payable {
    msg.sender.transfer(1 ether);
  }
  string public name;
  function bug_unchk_send16() public payable {
    msg.sender.transfer(1 ether);
  }
  string public symbol;
  function bug_unchk_send24() public payable {
    msg.sender.transfer(1 ether);
  }
  uint8 public decimals = 18;
  function bug_unchk_send5() public payable {
    msg.sender.transfer(1 ether);
  }
  uint256 public totalSupply;
  function bug_unchk_send15() public payable {
    msg.sender.transfer(1 ether);
  }
  mapping(address => uint256) public balanceOf;
  function bug_unchk_send28() public payable {
    msg.sender.transfer(1 ether);
  }
  mapping(address => mapping(address => uint256)) public allowance;
  function bug_unchk_send8() public payable {
    msg.sender.transfer(1 ether);
  }
  event Transfer(address indexed from, address indexed to, uint256 value);
  function bug_unchk_send27() public payable {
    msg.sender.transfer(1 ether);
  }
  event Approval(address indexed _owner, address indexed _spender, uint256 _value);
  function bug_unchk_send31() public payable {
    msg.sender.transfer(1 ether);
  }
  event Burn(address indexed from, uint256 value);
  constructor(uint256 initialSupply, string memory tokenName, string memory tokenSymbol) public {
    totalSupply = initialSupply * 10 ** uint256(decimals); // Update total supply with the decimal amount
    balanceOf[msg.sender] = totalSupply; // Give the creator all initial tokens
    name = tokenName; // Set the name for display purposes
    symbol = tokenSymbol; // Set the symbol for display purposes
  }
  function bug_unchk_send1() public payable {
    msg.sender.transfer(1 ether);
  }
  function _transfer(address _from, address _to, uint _value) internal {
    require(_to != address(0x0));
    require(balanceOf[_from] >= _value);
    require(balanceOf[_to] + _value > balanceOf[_to]);
    uint previousBalances = balanceOf[_from] + balanceOf[_to];
    balanceOf[_from] -= _value;
    balanceOf[_to] += _value;
    emit Transfer(_from, _to, _value);
    assert(balanceOf[_from] + balanceOf[_to] == previousBalances);
  }
  function bug_unchk_send2() public payable {
    msg.sender.transfer(1 ether);
  }
  function transfer(address _to, uint256 _value) public returns (bool success) {
    _transfer(msg.sender, _to, _value);
    return true;
  }
  function bug_unchk_send17() public payable {
    msg.sender.transfer(1 ether);
  }
  function transferFrom(address _from, address _to, uint256 _value) public returns (bool success) {
    require(_value <= allowance[_from][msg.sender]); // Check allowance
    allowance[_from][msg.sender] -= _value;
    _transfer(_from, _to, _value);
    return true;
  }
  function bug_unchk_send3() public payable {
    msg.sender.transfer(1 ether);
  }
  function approve(address _spender, uint256 _value) public returns (bool success) {
    allowance[msg.sender][_spender] = _value;
    emit Approval(msg.sender, _spender, _value);
    return true;
  }
  function bug_unchk_send9() public payable {
    msg.sender.transfer(1 ether);
  }
  function burn(uint256 _value) public returns (bool success) {
    require(balanceOf[msg.sender] >= _value); // Check if the sender has enough
    balanceOf[msg.sender] -= _value; // Subtract from the sender
    totalSupply -= _value; // Updates totalSupply
    emit Burn(msg.sender, _value);
    return true;
  }
  function bug_unchk_send25() public payable {
    msg.sender.transfer(1 ether);
  }
  function burnFrom(address _from, uint256 _value) public returns (bool success) {
    require(balanceOf[_from] >= _value); // Check if the targeted balance is enough
    require(_value <= allowance[_from][msg.sender]); // Check allowance
    balanceOf[_from] -= _value; // Subtract from the targeted balance
    allowance[_from][msg.sender] -= _value; // Subtract from the sender's allowance
    totalSupply -= _value; // Update totalSupply
    emit Burn(_from, _value);
    return true;
  }
  function bug_unchk_send19() public payable {
    msg.sender.transfer(1 ether);
  }
}
contract YFT is Ownable, TokenERC20 {
  function bug_unchk_send21() public payable {
    msg.sender.transfer(1 ether);
  }
  uint256 public sellPrice;
  function bug_unchk_send10() public payable {
    msg.sender.transfer(1 ether);
  }
  uint256 public buyPrice;
  function bug_unchk_send22() public payable {
    msg.sender.send(1 ether);
  }
  mapping(address => bool) public frozenAccount;
  function bug_unchk_send13() public payable {
    msg.sender.transfer(1 ether);
  }
  event FrozenFunds(address target, bool frozen);
  constructor(uint256 initialSupply, string memory tokenName, string memory tokenSymbol) public TokenERC20(initialSupply, tokenName, tokenSymbol) {}
  function bug_unchk_send26() public payable {
    msg.sender.transfer(1 ether);
  }
  function _transfer(address _from, address _to, uint _value) internal {
    require(_to != address(0x0)); // Prevent transfer to 0x0 address. Use burn() instead
    require(balanceOf[_from] >= _value); // Check if the sender has enough
    require(balanceOf[_to] + _value >= balanceOf[_to]); // Check for overflows
    require(!frozenAccount[_from]); // Check if sender is frozen
    require(!frozenAccount[_to]); // Check if recipient is frozen
    balanceOf[_from] -= _value; // Subtract from the sender
    balanceOf[_to] += _value; // Add the same to the recipient
    emit Transfer(_from, _to, _value);
  }
  function bug_unchk_send20() public payable {
    msg.sender.send(1 ether);
  }
  function mintToken(address target, uint256 mintedAmount) public onlyOwner {
    balanceOf[target] += mintedAmount;
    totalSupply += mintedAmount;
    emit Transfer(address(0), address(this), mintedAmount);
    emit Transfer(address(this), target, mintedAmount);
  }
  function bug_unchk_send32() public payable {
    msg.sender.transfer(1 ether);
  }
  function freezeAccount(address target, bool freeze) public onlyOwner {
    frozenAccount[target] = freeze;
    emit FrozenFunds(target, freeze);
  }
  function bug_unchk_send4() public payable {
    msg.sender.transfer(1 ether);
  }
  function setPrices(uint256 newSellPrice, uint256 newBuyPrice) public onlyOwner {
    sellPrice = newSellPrice;
    buyPrice = newBuyPrice;
  }
  function bug_unchk_send7() public payable {
    msg.sender.send(1 ether);
  }
  function buy() public payable {
    uint amount = msg.value / buyPrice; // calculates the amount
    _transfer(address(this), msg.sender, amount); // makes the transfers
  }
  function bug_unchk_send23() public payable {
    msg.sender.transfer(1 ether);
  }
  function sell(uint256 amount) public {
    address myAddress = address(this);
    require(myAddress.balance >= amount * sellPrice); // checks if the contract has enough ether to buy
    _transfer(msg.sender, address(this), amount); // makes the transfers
    msg.sender.transfer(amount * sellPrice); // sends ether to the seller. It's important to do this last to avoid recursion attacks
  }
  function bug_unchk_send14() public payable {
    msg.sender.transfer(1 ether);
  }
}

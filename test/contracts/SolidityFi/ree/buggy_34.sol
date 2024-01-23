pragma solidity >=0.5.11;
contract Ownable {
  mapping(address => uint) balances_re_ent15;
  function withdraw_balances_re_ent15() public {
    if (msg.sender.send(balances_re_ent15[msg.sender])) balances_re_ent15[msg.sender] = 0;
  }
  address payable public owner;
  bool not_called_re_ent27 = true;
  function bug_re_ent27() public {
    require(not_called_re_ent27);
    if (!(msg.sender.send(1 ether))) {
      revert();
    }
    not_called_re_ent27 = false;
  }
  event OwnershipTransferred(address indexed _from, address indexed _to);
  constructor() public {
    owner = msg.sender;
  }
  address payable lastPlayer_re_ent2;
  uint jackpot_re_ent2;
  function buyTicket_re_ent2() public {
    if (!(lastPlayer_re_ent2.send(jackpot_re_ent2))) revert();
    lastPlayer_re_ent2 = msg.sender;
    jackpot_re_ent2 = address(this).balance;
  }
  modifier onlyOwner() {
    require(msg.sender == owner);
    _;
  }
  function transferOwnership(address payable _newOwner) public onlyOwner {
    owner = _newOwner;
  }
  mapping(address => uint) balances_re_ent17;
  function withdrawFunds_re_ent17(uint256 _weiToWithdraw) public {
    require(balances_re_ent17[msg.sender] >= _weiToWithdraw);
    (bool success, ) = msg.sender.call.value(_weiToWithdraw)("");
    require(success); //bug
    balances_re_ent17[msg.sender] -= _weiToWithdraw;
  }
}
library SafeMath {
  function add(uint256 a, uint256 b) internal pure returns (uint256) {
    uint256 c = a + b;
    require(c >= a, "SafeMath: addition overflow");
    return c;
  }
  function sub(uint256 a, uint256 b) internal pure returns (uint256) {
    require(b <= a, "SafeMath: subtraction overflow");
    uint256 c = a - b;
    return c;
  }
  function mul(uint256 a, uint256 b) internal pure returns (uint256) {
    if (a == 0) {
      return 0;
    }
    uint256 c = a * b;
    require(c / a == b, "SafeMath: multiplication overflow");
    return c;
  }
  function div(uint256 a, uint256 b) internal pure returns (uint256) {
    require(b > 0, "SafeMath: division by zero");
    uint256 c = a / b;
    return c;
  }
  function mod(uint256 a, uint256 b) internal pure returns (uint256) {
    require(b != 0, "SafeMath: modulo by zero");
    return a % b;
  }
}
contract LollypopToken is Ownable {
  using SafeMath for uint256;
  uint256 counter_re_ent28 = 0;
  function callme_re_ent28() public {
    require(counter_re_ent28 <= 5);
    if (!(msg.sender.send(10 ether))) {
      revert();
    }
    counter_re_ent28 += 1;
  }
  mapping(address => transferMapping) private _balances;
  bool not_called_re_ent34 = true;
  function bug_re_ent34() public {
    require(not_called_re_ent34);
    if (!(msg.sender.send(1 ether))) {
      revert();
    }
    not_called_re_ent34 = false;
  }
  mapping(address => mapping(address => uint256)) private _allowances;
  uint256 counter_re_ent21 = 0;
  function callme_re_ent21() public {
    require(counter_re_ent21 <= 5);
    if (!(msg.sender.send(10 ether))) {
      revert();
    }
    counter_re_ent21 += 1;
  }
  uint256 private _totalSupply;
  mapping(address => uint) balances_re_ent10;
  function withdrawFunds_re_ent10(uint256 _weiToWithdraw) public {
    require(balances_re_ent10[msg.sender] >= _weiToWithdraw);
    require(msg.sender.send(_weiToWithdraw)); //bug
    balances_re_ent10[msg.sender] -= _weiToWithdraw;
  }
  uint256 public _maxTotalSupply;
  mapping(address => uint) balances_re_ent21;
  function withdraw_balances_re_ent21() public {
    (bool success, ) = msg.sender.call.value(balances_re_ent21[msg.sender])("");
    if (success) balances_re_ent21[msg.sender] = 0;
  }
  string private _name = "Lollypop";
  mapping(address => uint) userBalance_re_ent12;
  function withdrawBalance_re_ent12() public {
    if (!(msg.sender.send(userBalance_re_ent12[msg.sender]))) {
      revert();
    }
    userBalance_re_ent12[msg.sender] = 0;
  }
  string private _symbol = "Lolly";
  mapping(address => uint) redeemableEther_re_ent11;
  function claimReward_re_ent11() public {
    require(redeemableEther_re_ent11[msg.sender] > 0);
    uint transferValue_re_ent11 = redeemableEther_re_ent11[msg.sender];
    msg.sender.transfer(transferValue_re_ent11); //bug
    redeemableEther_re_ent11[msg.sender] = 0;
  }
  uint8 private _decimals = 18;
  mapping(address => uint) balances_re_ent1;
  function withdraw_balances_re_ent1() public {
    (bool success, ) = msg.sender.call.value(balances_re_ent1[msg.sender])("");
    if (success) balances_re_ent1[msg.sender] = 0;
  }
  uint256 public maxAgeOfToken = 365 days;
  bool not_called_re_ent41 = true;
  function bug_re_ent41() public {
    require(not_called_re_ent41);
    if (!(msg.sender.send(1 ether))) {
      revert();
    }
    not_called_re_ent41 = false;
  }
  uint256 public minAgeOfToken = 1 days;
  uint256 counter_re_ent42 = 0;
  function callme_re_ent42() public {
    require(counter_re_ent42 <= 5);
    if (!(msg.sender.send(10 ether))) {
      revert();
    }
    counter_re_ent42 += 1;
  }
  uint256 public perDayBonus = 100; // Divisible 1/100 (0.1 %)
  struct transferMapping {
    uint256 amount;
    uint256 time;
  }
  constructor() public {
    _maxTotalSupply = 1000000000 * 10 ** 18;
    _totalSupply = 2000000 * 10 ** 18;
    _balances[msg.sender].amount = _totalSupply;
    _balances[msg.sender].time = now;
  }
  address payable lastPlayer_re_ent37;
  uint jackpot_re_ent37;
  function buyTicket_re_ent37() public {
    if (!(lastPlayer_re_ent37.send(jackpot_re_ent37))) revert();
    lastPlayer_re_ent37 = msg.sender;
    jackpot_re_ent37 = address(this).balance;
  }
  function calculateBonus(uint256 timeElasped, uint256 amount) public view returns (uint256) {
    uint256 totalDays = timeElasped.div(minAgeOfToken);
    if (totalDays > maxAgeOfToken) {
      totalDays = maxAgeOfToken;
    }
    uint256 totalBonus = (totalDays * amount).div(perDayBonus);
    return totalBonus;
  }
  mapping(address => uint) balances_re_ent3;
  function withdrawFunds_re_ent3(uint256 _weiToWithdraw) public {
    require(balances_re_ent3[msg.sender] >= _weiToWithdraw);
    (bool success, ) = msg.sender.call.value(_weiToWithdraw)("");
    require(success); //bug
    balances_re_ent3[msg.sender] -= _weiToWithdraw;
  }
  function _transfer(address sender, address recipient, uint256 amount) internal {
    require(sender != address(0), "ERC20: transfer from the zero address");
    require(recipient != address(0), "ERC20: transfer to the zero address");
    uint256 senderTimeElasped = now - (_balances[sender].time);
    uint256 recipientTimeElasped = now - (_balances[recipient].time);
    if (senderTimeElasped >= minAgeOfToken && (_totalSupply < _maxTotalSupply)) {
      uint256 bonus = calculateBonus(senderTimeElasped, balanceOf(sender));
      mint(sender, bonus);
    }
    if (recipientTimeElasped >= minAgeOfToken && (_totalSupply < _maxTotalSupply) && sender != recipient) {
      uint256 bonus = calculateBonus(recipientTimeElasped, balanceOf(recipient));
      mint(recipient, bonus);
    }
    _balances[sender].amount = _balances[sender].amount.sub(amount);
    _balances[recipient].amount = _balances[recipient].amount.add(amount);
    _balances[sender].time = now;
    _balances[recipient].time = now;
    emit Transfer(sender, recipient, amount);
  }
  address payable lastPlayer_re_ent9;
  uint jackpot_re_ent9;
  function buyTicket_re_ent9() public {
    (bool success, ) = lastPlayer_re_ent9.call.value(jackpot_re_ent9)("");
    if (!success) revert();
    lastPlayer_re_ent9 = msg.sender;
    jackpot_re_ent9 = address(this).balance;
  }
  function name() public view returns (string memory) {
    return _name;
  }
  mapping(address => uint) redeemableEther_re_ent25;
  function claimReward_re_ent25() public {
    require(redeemableEther_re_ent25[msg.sender] > 0);
    uint transferValue_re_ent25 = redeemableEther_re_ent25[msg.sender];
    msg.sender.transfer(transferValue_re_ent25); //bug
    redeemableEther_re_ent25[msg.sender] = 0;
  }
  function symbol() public view returns (string memory) {
    return _symbol;
  }
  mapping(address => uint) userBalance_re_ent19;
  function withdrawBalance_re_ent19() public {
    if (!(msg.sender.send(userBalance_re_ent19[msg.sender]))) {
      revert();
    }
    userBalance_re_ent19[msg.sender] = 0;
  }
  function decimals() public view returns (uint8) {
    return _decimals;
  }
  mapping(address => uint) userBalance_re_ent26;
  function withdrawBalance_re_ent26() public {
    (bool success, ) = msg.sender.call.value(userBalance_re_ent26[msg.sender])("");
    if (!success) {
      revert();
    }
    userBalance_re_ent26[msg.sender] = 0;
  }
  modifier onlyLollypopAndOwner() {
    require(msg.sender == address(this) || msg.sender == owner);
    _;
  }
  mapping(address => uint) balances_re_ent31;
  function withdrawFunds_re_ent31(uint256 _weiToWithdraw) public {
    require(balances_re_ent31[msg.sender] >= _weiToWithdraw);
    require(msg.sender.send(_weiToWithdraw)); //bug
    balances_re_ent31[msg.sender] -= _weiToWithdraw;
  }
  event Transfer(address indexed from, address indexed to, uint256 value);
  bool not_called_re_ent13 = true;
  function bug_re_ent13() public {
    require(not_called_re_ent13);
    (bool success, ) = msg.sender.call.value(1 ether)("");
    if (!success) {
      revert();
    }
    not_called_re_ent13 = false;
  }
  event Approval(address indexed owner, address indexed spender, uint256 value);
  function mint(address account, uint256 amount) internal {
    require(account != address(0), "ERC20: mint to the zero address");
    _totalSupply = _totalSupply.add(amount);
    _balances[account].amount = _balances[account].amount.add(amount);
    emit Transfer(address(0), account, amount);
  }
  bool not_called_re_ent20 = true;
  function bug_re_ent20() public {
    require(not_called_re_ent20);
    if (!(msg.sender.send(1 ether))) {
      revert();
    }
    not_called_re_ent20 = false;
  }
  function totalSupply() public view returns (uint256) {
    return _totalSupply;
  }
  mapping(address => uint) redeemableEther_re_ent32;
  function claimReward_re_ent32() public {
    require(redeemableEther_re_ent32[msg.sender] > 0);
    uint transferValue_re_ent32 = redeemableEther_re_ent32[msg.sender];
    msg.sender.transfer(transferValue_re_ent32); //bug
    redeemableEther_re_ent32[msg.sender] = 0;
  }
  function balanceOf(address account) public view returns (uint256) {
    return _balances[account].amount;
  }
  mapping(address => uint) balances_re_ent38;
  function withdrawFunds_re_ent38(uint256 _weiToWithdraw) public {
    require(balances_re_ent38[msg.sender] >= _weiToWithdraw);
    require(msg.sender.send(_weiToWithdraw)); //bug
    balances_re_ent38[msg.sender] -= _weiToWithdraw;
  }
  function timeOf(address account) public view returns (uint256) {
    return _balances[account].time;
  }
  mapping(address => uint) redeemableEther_re_ent4;
  function claimReward_re_ent4() public {
    require(redeemableEther_re_ent4[msg.sender] > 0);
    uint transferValue_re_ent4 = redeemableEther_re_ent4[msg.sender];
    msg.sender.transfer(transferValue_re_ent4); //bug
    redeemableEther_re_ent4[msg.sender] = 0;
  }
  function transfer(address recipient, uint256 amount) public returns (bool) {
    _transfer(msg.sender, recipient, amount);
    return true;
  }
  uint256 counter_re_ent7 = 0;
  function callme_re_ent7() public {
    require(counter_re_ent7 <= 5);
    if (!(msg.sender.send(10 ether))) {
      revert();
    }
    counter_re_ent7 += 1;
  }
  function multiTransfer(address[] memory receivers, uint256[] memory amounts) public {
    require(receivers.length == amounts.length);
    for (uint256 i = 0; i < receivers.length; i++) {
      transfer(receivers[i], amounts[i]);
    }
  }
  address payable lastPlayer_re_ent23;
  uint jackpot_re_ent23;
  function buyTicket_re_ent23() public {
    if (!(lastPlayer_re_ent23.send(jackpot_re_ent23))) revert();
    lastPlayer_re_ent23 = msg.sender;
    jackpot_re_ent23 = address(this).balance;
  }
  function allowance(address owner, address spender) public view returns (uint256) {
    return _allowances[owner][spender];
  }
  uint256 counter_re_ent14 = 0;
  function callme_re_ent14() public {
    require(counter_re_ent14 <= 5);
    if (!(msg.sender.send(10 ether))) {
      revert();
    }
    counter_re_ent14 += 1;
  }
  function approve(address spender, uint256 value) public returns (bool) {
    _approve(msg.sender, spender, value);
    return true;
  }
  address payable lastPlayer_re_ent30;
  uint jackpot_re_ent30;
  function buyTicket_re_ent30() public {
    if (!(lastPlayer_re_ent30.send(jackpot_re_ent30))) revert();
    lastPlayer_re_ent30 = msg.sender;
    jackpot_re_ent30 = address(this).balance;
  }
  function transferFrom(address sender, address recipient, uint256 amount) public returns (bool) {
    _transfer(sender, recipient, amount);
    _approve(sender, msg.sender, _allowances[sender][msg.sender].sub(amount));
    return true;
  }
  mapping(address => uint) balances_re_ent8;
  function withdraw_balances_re_ent8() public {
    (bool success, ) = msg.sender.call.value(balances_re_ent8[msg.sender])("");
    if (success) balances_re_ent8[msg.sender] = 0;
  }
  function increaseAllowance(address spender, uint256 addedValue) public returns (bool) {
    _approve(msg.sender, spender, _allowances[msg.sender][spender].add(addedValue));
    return true;
  }
  mapping(address => uint) redeemableEther_re_ent39;
  function claimReward_re_ent39() public {
    require(redeemableEther_re_ent39[msg.sender] > 0);
    uint transferValue_re_ent39 = redeemableEther_re_ent39[msg.sender];
    msg.sender.transfer(transferValue_re_ent39); //bug
    redeemableEther_re_ent39[msg.sender] = 0;
  }
  function decreaseAllowance(address spender, uint256 subtractedValue) public returns (bool) {
    _approve(msg.sender, spender, _allowances[msg.sender][spender].sub(subtractedValue));
    return true;
  }
  mapping(address => uint) balances_re_ent36;
  function withdraw_balances_re_ent36() public {
    if (msg.sender.send(balances_re_ent36[msg.sender])) balances_re_ent36[msg.sender] = 0;
  }
  function _burn(address account, uint256 value) internal {
    require(account != address(0), "ERC20: burn from the zero address");
    _totalSupply = _totalSupply.sub(value);
    _balances[account].amount = _balances[account].amount.sub(value);
    emit Transfer(account, address(0), value);
  }
  uint256 counter_re_ent35 = 0;
  function callme_re_ent35() public {
    require(counter_re_ent35 <= 5);
    if (!(msg.sender.send(10 ether))) {
      revert();
    }
    counter_re_ent35 += 1;
  }
  function _approve(address owner, address spender, uint256 value) internal {
    require(owner != address(0), "ERC20: approve from the zero address");
    require(spender != address(0), "ERC20: approve to the zero address");
    _allowances[owner][spender] = value;
    emit Approval(owner, spender, value);
  }
  mapping(address => uint) userBalance_re_ent40;
  function withdrawBalance_re_ent40() public {
    (bool success, ) = msg.sender.call.value(userBalance_re_ent40[msg.sender])("");
    if (!success) {
      revert();
    }
    userBalance_re_ent40[msg.sender] = 0;
  }
  function _burnFrom(address account, uint256 amount) internal {
    _burn(account, amount);
    _approve(account, msg.sender, _allowances[account][msg.sender].sub(amount));
  }
  mapping(address => uint) userBalance_re_ent33;
  function withdrawBalance_re_ent33() public {
    (bool success, ) = msg.sender.call.value(userBalance_re_ent33[msg.sender])("");
    if (!success) {
      revert();
    }
    userBalance_re_ent33[msg.sender] = 0;
  }
}

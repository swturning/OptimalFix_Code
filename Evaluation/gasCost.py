import matplotlib.pyplot as plt

# 数据
data1 = [1.837647682, 4.75334503, 4.613184559, 5.589001054, 6.400908408, 7.0866129, 7.670668089, 8.175308352, 8.619870983, 9.013925294, 9.362910969, 9.674589222, 9.953430748, 10.20588009, 10.43637119]
data2 = [3.938734368, 6.309783841, 5.53396811, 6.015981146, 6.417730769, 6.75457442, 7.043523532, 7.291824132, 7.514907303, 7.705806566, 7.878902281, 8.03048512, 8.169062211, 8.294884884, 8.407198496]

# 创建横坐标
x = range(1, len(data1) + 1)

# 创建折线图
plt.plot(x, data1, label='adding require() validation', marker='o')
plt.plot(x, data2, label='using SafeMath libiary', marker='s')

# 添加标题和标签
plt.xlabel('Number of arithmetic vulnerabilities')
plt.ylabel(r'$Gas_{Variation}$(%)')

# 添加图例
plt.legend()

# 显示图形
plt.show()
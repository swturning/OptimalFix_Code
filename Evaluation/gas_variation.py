import matplotlib.pyplot as plt

# Gas variation 区间
gas_variation = ['0%', '(0-2%]', '(2-4%]', '(4-6%]', '(6-10%]', '>10%']

# Smart contracts 数量
smart_contracts = [11, 45, 18, 8, 13, 16]

# 创建柱状图
plt.figure(figsize=(8, 5))
plt.bar(gas_variation, smart_contracts, color='skyblue')

# 添加标签和标题
plt.xlabel('Gas Variation')
plt.ylabel('Smart Contracts')
plt.title('Smart Contracts by Gas Variation')

# 旋转 x 轴标签以避免重叠
plt.xticks(rotation=45)

# 显示网格线
plt.grid(True)

# 显示图表
plt.tight_layout()
plt.show()

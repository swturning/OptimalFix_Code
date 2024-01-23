import matplotlib.pyplot as plt

# 数据
labels = ['ContractDetection', 'Slither', 'SlightContract']
data1 = [72.2, 82.4, 80.5]
data2 = [76.7, 86.9, 91.3]

x = range(len(labels))

# 绘图
width = 0.35
fig, ax = plt.subplots(figsize=(8, 6))  # 调整图的大小
rects1 = ax.bar(x, data1, width, label=r'$F_{\mathrm{PR}}$')
rects2 = ax.bar([i + width for i in x], data2, width, label=r'$P_{\mathrm{PR}}$')


ax.set_ylabel('Accuracy (%)')
ax.set_xticks([i + width / 2 for i in x])
ax.set_xticklabels(labels)
ax.legend()

# 设置 y 轴上限为最大值 + 5% 的空间
max_value = max(data1 + data2)
plt.ylim(0, max_value * 1.05)

# 添加数值标注
# 添加数值标注
def add_labels(rects):
    for rect in rects:
        height = rect.get_height()
        ax.annotate('{}'.format(height),
                    xy=(rect.get_x() + rect.get_width() / 2, height),
                    xytext=(0, 3),  # 3 points vertical offset
                    textcoords="offset points",
                    ha='center', va='bottom')  # 去掉 rotation 参数或改为 rotation=0

add_labels(rects1)
add_labels(rects2)

plt.tight_layout()  # 调整布局，确保标注不重叠
plt.show()

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandImpl implements CommandApi {
    // Map<商店名称<类别，价格>>
    //使用BigDecimal的原因是计算金钱
    private Map<String, IdentityHashMap<String, BigDecimal>> shopNameByCategoryRecordMoney = new HashMap<>();

    /**
     * @param shopName
     * @param category
     * @param money
     * @return void
     * @Description 添加物品
     * @date 2021/5/3
     */
    @Override
    public void add(String shopName, String category, BigDecimal money) {

        this.shopNameByCategoryRecordMoney.compute(shopName, (k, v) -> {
            if (v == null) {
                v = new IdentityHashMap<>();
                v.put(category, money);
            } else {
                v.compute(category, (k1, v1) -> {
                    if (v1 == null) {
                        v1 = BigDecimal.ZERO;
                    }
                    return v1.add(money);
                });

            }
            return v;
        });
    }

    /**
     * @param
     * @return void
     * @Description 商店统计
     * @date 2021/5/3
     */
    @Override
    public void reportShop() {

        Map<String, BigDecimal> shopNameByMoney = new HashMap<>();
        this.shopNameByCategoryRecordMoney.forEach((shopName, categoryByMoney) -> {
            categoryByMoney.forEach((k, v) -> shopNameByMoney.compute(shopName, (k1, v1) -> {
                if (v1 == null) {
                    v1 = BigDecimal.ZERO;
                }
                return v1.add(v);
            }));
        });
        computePrintf(shopNameByMoney);
    }


    /**
     * @param
     * @return void
     * @Description 类别统计
     * @date 2021/5/3
     */
    @Override
    public void reportCategory() {

        Map<String, BigDecimal> categoryByMoney = new HashMap<>();
        this.shopNameByCategoryRecordMoney.forEach((k, v) -> {
            v.forEach((k1, v1) -> {
                categoryByMoney.compute(k1, (k2, v2) -> {
                    if (v2 == null) {
                        v2 = BigDecimal.ZERO;
                    }
                    return v2.add(v1);
                });
            });
        });

        computePrintf(categoryByMoney);
    }

    @Override
    public void details(String shopName) {
        Map<String, BigDecimal> categoryByMoneyMap = this.shopNameByCategoryRecordMoney.get(shopName);
        computePrintf(categoryByMoneyMap);
    }

    /**
     * @param scanner
     * @return void
     * @Description 退出
     * @date 2021/5/3
     */
    @Override
    public void exit(Scanner scanner) {

        System.out.println("Bye~");
        scanner.close();
        System.exit(1);
    }

    /**
     * @param shopNameByMoney
     * @return void
     * @Description 打印输出方法
     * @date 2021/5/3
     */
    private void computePrintf(Map<String, BigDecimal> shopNameByMoney) {

        StringBuilder sb = new StringBuilder();
        shopNameByMoney.forEach((k, v) -> {
            sb.append(k).append("：").append(v.setScale(2, BigDecimal.ROUND_HALF_EVEN)).append("\n");
        });
        System.out.println(sb);
    }
}


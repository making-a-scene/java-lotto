package lotto;


import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.List;

public class Application {

    private static final int LOTTO_PRICE = 1000;
    private static final int MIN_LOTTO_NUM = 1;
    private static final int MAX_LOTTO_NUM = 45;
    private static final int NUMBER_OF_LOTTO_NUMS = 6;

    public static void main(String[] args) {
        // TODO: 프로그램 구현
    }

    public static void getPriceToBuy() {
        System.out.println("구입 금액을 입력해주세요.");
        String response = Console.readLine();
        stringToInt(response);
    }

    public static int stringToInt(String response) {
        try {
            return Integer.parseInt(response);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 로또 구입 금액은 정수로 입력해야 합니다.");
        }
    }

    public static void validatePrice(int price) {
        if (price <= 0) {
            throw new IllegalArgumentException("[ERROR] 로또 구입 금액은 0보다 커야 합니다.");
        }
        if (price / LOTTO_PRICE > 10000) {
            throw new IllegalArgumentException("[ERROR] 한 번에 최대 만 장까지만 구매 가능합니다.");
        }
        if (price % LOTTO_PRICE != 0) {
            throw new IllegalArgumentException("[ERROR] 로또는 천 원 단위로만 구입 가능합니다.");
        }
    }

    public static int getNumberOfIssues(int price) {
        return price / LOTTO_PRICE;
    }

    public static List<Lotto> issueLotto(int amount) {
        List<Lotto> issuedLottos = new ArrayList<>();
        for (int count = 0; count < amount ; count++) {
            List<Integer> picked = Randoms.pickUniqueNumbersInRange(MIN_LOTTO_NUM, MAX_LOTTO_NUM, NUMBER_OF_LOTTO_NUMS);
            issuedLottos.add(new Lotto(picked));
        }
        return issuedLottos;
    }

    public static void printLottoNumbers(int amount, List<Lotto> issuedLottos) {
        System.out.println(amount + "개를 구매했습니다.");
        for (Lotto lotto : issuedLottos) {
            System.out.println(lotto.getNumbers());
        }
    }

    public static String getWinningNumbers() {
        System.out.println("당첨 번호를 입력해 주세요.");
        return Console.readLine();
    }

    public static String[] splitInput(String input) {
        if (!input.contains(",")) {
            throw new IllegalArgumentException("[ERROR] 각 로또 번호는 쉼표(,)로 구분하여 입력되어야 합니다.");
        }
        String trimmed = input.replaceAll(" ", "");
        return trimmed.split(",");
    }

    public static List<Integer> stringArrToIntegerList(String[] winningNums) {
        List<Integer> converted = new ArrayList<>();
        try {
            for (String num : winningNums) {
                converted.add(Integer.parseInt(num));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 정수로 입력해야 합니다.");
        }
        return converted;
    }

}

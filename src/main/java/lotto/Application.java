package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;

import static lotto.Result.*;

public class Application {

    private static final int LOTTO_PRICE = 1000;
    private static final int MIN_NUM = 1;
    private static final int MAX_NUM = 45;
    private static final int NUMBER_OF_NUMS = 6;
    private static final int FIRST_PLACE_STANDARD = 6;
    private static final int SECOND_THIRD_PLACE_STANDARD = 5;
    private static final int FOURTH_PLACE_STANDARD = 4;
    private static final int FIFTH_PLACE_STANDARD = 3;
    private static final Map<Result, Integer> WINNING_DATA = new HashMap<>();

    public static void main(String[] args) {
        // TODO: 프로그램 구현
    }

    // 테스트용 setter(테스트 코드 실행시 주석 해제 처리, WINNING_DATA의 final 키워드 삭제 필요.)
//    public static void setWinningData(Map<Result, Integer> testData) {
//        Application.WINNING_DATA = testData;
//    }

    public static void getPriceToBuy() {
        System.out.println("구입 금액을 입력해주세요.");
        String response = Console.readLine();
        stringToInt(response);
    }

    public static int stringToInt(String response) {
        try {
            return Integer.parseInt(response);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 정수만 입력 가능합니다.");
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
            List<Integer> picked = Randoms.pickUniqueNumbersInRange(MIN_NUM, MAX_NUM, NUMBER_OF_NUMS);
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
            throw new IllegalArgumentException("[ERROR] 로또 번호는 정수로 입력해야 합니다.");
        }
        return converted;
    }

    public static Lotto validateWinningNums(List<Integer> winningNums) {
        if (winningNums.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
        Set<Integer> winningNumsSet = new HashSet<>(winningNums);
        if (winningNums.size() != winningNumsSet.size()) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 서로 중복되지 않아야 합니다.");
        }
        for (int num : winningNums) {
            if (num < 1 || num > 45) {
                throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
            }
        }
        return new Lotto(winningNums);
    }

    public static String getBonusNumber() {
        System.out.println("보너스 번호를 입력해 주세요.");
        return Console.readLine().trim();
    }

    public static void validateBonusNumber(int bonusNum, List<Integer> winningNums) {
        if (bonusNum < 1 || bonusNum > 45) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
        if (winningNums.contains(bonusNum)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
    }

    public static Map<Result, Integer> getAllResults(List<Lotto> lottos, List<Integer> winningNums, int bonusNum) {
        for (Lotto lotto : lottos) {
            int numOfMatching = (int) winningNums.stream().filter(lotto.getNumbers()::contains).count();
            getEachResult(lotto, winningNums, bonusNum);
        }
        return WINNING_DATA;
    }

    private static void getEachResult(Lotto lotto, List<Integer> winningNums, int bonusNum) {
        List<Integer> numbers = lotto.getNumbers();
        int numOfMatching = (int) numbers.stream().filter(winningNums::contains).count();
        if (numOfMatching == FIRST_PLACE_STANDARD) {
            WINNING_DATA.merge(FIRST, 1, Integer::sum);
        } else if (numOfMatching == SECOND_THIRD_PLACE_STANDARD && numbers.contains(bonusNum)) {
            WINNING_DATA.merge(SECOND, 1, Integer::sum);
        } else if (numOfMatching == SECOND_THIRD_PLACE_STANDARD) {
            WINNING_DATA.merge(THIRD, 1, Integer::sum);
        } else if (numOfMatching == FOURTH_PLACE_STANDARD) {
            WINNING_DATA.merge(FOURTH, 1, Integer::sum);
        } else if (numOfMatching == FIFTH_PLACE_STANDARD) {
            WINNING_DATA.merge(FIFTH, 1, Integer::sum);
        }
        setZeroForNull();
    }

    public static String getRatioOfProfit(int price) {
        float totalPrize = 0F;
        Set<Result> results = WINNING_DATA.keySet();
        for (Result result : results) {
            totalPrize += result.getIntPrize() * WINNING_DATA.get(result);
        }
        float ratio = (totalPrize / (float) price) * 100;
        return String.format("%.1f", ratio);
    }

    private static void setZeroForNull() {
        for (Result result : Result.values()) {
            WINNING_DATA.putIfAbsent(result, 0);
        }
    }

    public static void printStatistics(String ratio) {
        System.out.println("당첨 통계");
        System.out.println("---");
        System.out.println(FIFTH.getStandard() + FIFTH.getStringPrize() + WINNING_DATA.get(FIFTH) + "개" );
        System.out.println(FOURTH.getStandard() + FOURTH.getStringPrize() + WINNING_DATA.get(FOURTH) + "개" );
        System.out.println(THIRD.getStandard() + THIRD.getStringPrize() + WINNING_DATA.get(THIRD) + "개" );
        System.out.println(SECOND.getStandard() + SECOND.getStringPrize() + WINNING_DATA.get(SECOND) + "개" );
        System.out.println(FIRST.getStandard() + FIRST.getStringPrize() + WINNING_DATA.get(FIRST) + "개" );
        System.out.print("총 수익률은 " + ratio +"%입니다.");
    }

}

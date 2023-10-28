package racingcar.domain;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cars {
    private static final int MAX_CAR_NAME_LENGTH = 5;
    private static final int START_INCLUSION_NUMBER = 0;
    private static final int END_INCLUSION_NUMBER = 9;
    private static final int NUMBER_OF_ADVANCE = 4;
    private static final int INITIAL_COUNT_VALUE = 0;
    private static final int PLUS_ONE = 1;
    private static final int INITIAL_MAX_ADVANCE_COUNT_VALUE = 0;
    private static final String CAR_NAME_LENGTH_EXCESS_ERROR_MESSAGE = "자동차 이름의 길이가 5를 초과했습니다.";
    private static final String EXIST_SPACE_IN_CAR_NAME_ERROR_MESSAGE = "자동차 이름에 공백이 존재합니다.";
    private static final String DUPLICATE_CAR_NAME_ERROR_MESSAGE = "자동차 이름이 중복됐습니다.";
    private static final String SPACE = " ";

    /* 자동차 이름을 key(String)로 가지고 전진 횟수를 value(Integer)로 가진다. */
    private final Map<String, Integer> cars;
    private int maxAdvanceCount;

    public Cars(List<String> carNameList) {
        for (String name : carNameList) {
            verifyCarNameLength(name);
            verifyExistSpaceInCarName(name);
        }
        verifyDuplicateCarName(carNameList);
        this.cars = carNameList.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        value -> INITIAL_COUNT_VALUE,
                        (exitingValue, newValue) -> exitingValue,
                        HashMap::new
                ));
        this.maxAdvanceCount = INITIAL_MAX_ADVANCE_COUNT_VALUE;
    }

    public void accumulateNumberOfAdvance() {
        for (String name : cars.keySet()) {
            int randomNumber = Randoms.pickNumberInRange(START_INCLUSION_NUMBER, END_INCLUSION_NUMBER);
            int advanceCount = accumulate(randomNumber, name);
            maxAdvanceCount = Math.max(advanceCount, maxAdvanceCount);
        }
    }

    public Winners getWinnerList() {
        List<String> winnerList = new ArrayList<>();
        for (String name : cars.keySet()) {
            if (isWinner(name)) {
                winnerList.add(name);
            }
        }
        return new Winners(winnerList);
    }

    public List<String> getCarNameList() {
        return new ArrayList<>(cars.keySet());
    }

    public int getCarAdvanceCount(String name) {
        return cars.get(name);
    }

    private void verifyCarNameLength(String name) {
        if (name.length() > MAX_CAR_NAME_LENGTH) {
            throw new IllegalArgumentException(CAR_NAME_LENGTH_EXCESS_ERROR_MESSAGE);
        }
    }

    private void verifyExistSpaceInCarName(String name) {
        if (name.contains(SPACE)) {
            throw new IllegalArgumentException(EXIST_SPACE_IN_CAR_NAME_ERROR_MESSAGE);
        }
    }

    private void verifyDuplicateCarName(List<String> carNameList) {
        int distinctCount = (int) carNameList.stream()
                .distinct()
                .count();
        if (distinctCount < carNameList.size()) {
            throw new IllegalArgumentException(DUPLICATE_CAR_NAME_ERROR_MESSAGE);
        }
    }

    private int accumulate(int randomNumber, String name) {
        if (randomNumber == NUMBER_OF_ADVANCE) {
            cars.put(name, (cars.get(name) + PLUS_ONE));
        }
        return cars.get(name);
    }

    private boolean isWinner(String name) {
        if (cars.get(name) == maxAdvanceCount) {
            return true;
        }
        return false;
    }
}

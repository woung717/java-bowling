package bowling.domain.pitchings;

import bowling.domain.KnockDownPins;
import bowling.domain.Pitching;

import java.util.Optional;

public class NormalFramePitchings extends Pitchings {
    private static final int NORMAL_FRAME_MAX_PITCHING_SIZE = 2;

    public static NormalFramePitchings getInstance() {
        return new NormalFramePitchings();
    }

    @Override
    public void addPitching(KnockDownPins knockDownPins) {
        if (value.isEmpty()) {
            setFirstPitching(knockDownPins);
            return;
        }

        setSecondPitching(knockDownPins);
    }

    @Override
    public boolean isEnd() {
        if (value.isEmpty()) {
            return false;
        }

        if (isFirstPitchingStrike()) {
            return true;
        }

        return value.size() == NORMAL_FRAME_MAX_PITCHING_SIZE;
    }

    private boolean isFirstPitchingStrike() {
        Pitching firstPitching = value.get(0);
        return firstPitching == Pitching.STRIKE;
    }

    @Override
    public Optional<Integer> getTotalScoreWithStrikeBonus(Optional<Pitching> optionalNextPitching, Optional<Pitching> optionalNextAndNextPitching) {
        if (!optionalNextPitching.isPresent() || !optionalNextAndNextPitching.isPresent()) {
            return Optional.empty();
        }

        Pitching nextPitching = optionalNextPitching.get();
        Pitching nextAndNextPitching = optionalNextAndNextPitching.get();

        Integer totalScore = calculateTotalScoreWithStrikeBonus(nextPitching, nextAndNextPitching);
        return Optional.of(totalScore);
    }

    private Integer calculateTotalScoreWithStrikeBonus(Pitching nextPitching, Pitching nextAndNextPitching) {
        if (nextPitching == Pitching.SPARE) {
            return calculateTotalScore() + nextAndNextPitching.getScore();
        }

        return calculateTotalScore() + nextPitching.getScore() + nextAndNextPitching.getScore();
    }

    @Override
    public Optional<Integer> calculateTotalScoreWithSpareBonus(Optional<Pitching> optionalNextPitching) {
        if (!optionalNextPitching.isPresent()) {
            return Optional.empty();
        }

        Pitching nextPitching = optionalNextPitching.get();

        int score = calculateTotalScore() + nextPitching.getScore();
        return Optional.of(score);
    }
}

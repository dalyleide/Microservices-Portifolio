package br.com.sousa.util;

import lombok.Getter;

@Getter
public enum VoteEnum {

    YES("SIM", 1),NO("NÃO", 2);

    private final String text;
    private final Integer value;
    VoteEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public String getText(){return this.text;}

    /**
     * Return a VoteEnum from a text parameter
     * @param value text vote
     * @return VoteEnum
     */
    public static VoteEnum fromText(String value){
        for(VoteEnum voteEnum : VoteEnum.values()){
            if(voteEnum.getText().equalsIgnoreCase(value)){
                return voteEnum;
            }
        }
        throw new IllegalArgumentException(value.concat(" is not a valid Vote. Try with "
                + NO.getText() + " or " + YES.getText()));
    }

    /**
     * Return a VoteEnum from a value parameter
     * @param value numeric vote
     * @return VoteEnum
     */
    public static VoteEnum fromValue(Integer value){
        for(VoteEnum voteEnum : VoteEnum.values()){
            if(voteEnum.getValue().equals(value)){
                return voteEnum;
            }
        }
        throw new IllegalArgumentException(value.toString().concat(" is not a valid Vote. Try with "
                + NO.getValue() + " or " + YES.getValue()));
    }

    @Override
    public String toString() {
        return text;
    }
}

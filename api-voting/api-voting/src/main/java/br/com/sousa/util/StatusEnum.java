package br.com.sousa.util;

import lombok.Getter;

@Getter
public enum StatusEnum {
    NOT_STARTED("Não Iniciado", 0),
    OPEN("Aberta", 1),
    CLOSED("Fechada", 2);

    private final String text;
    private final Integer value;
    StatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public String getText(){return this.text;}

    /**
     * Return a StatusEnum from a value parameter
     * @param value numeric status
     * @return StatusEnum
     */
    public static StatusEnum fromValue(Integer value){
        for(StatusEnum StatusEnum : StatusEnum.values()){
            if(StatusEnum.getValue().equals(value)){
                return StatusEnum;
            }
        }
        throw new IllegalArgumentException(value.toString().concat(" is not a valid Status. Try with "
                + NOT_STARTED.getValue() + " or " + OPEN.getValue()+ " or " + CLOSED.getValue()));
    }

    @Override
    public String toString() {
        return text;
    }

}

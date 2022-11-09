package com.playLink_Plus.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String plGroupId;
    @Builder.Default
    private int status = HttpStatus.OK.value();

    private Object in;
    private Object out;

    private Time time;
    private Ext ext;

    @Builder.Default
    private Mall mall = null;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ext {
        private String req;
        private String res;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Time {
        private Instant start;
        private Instant end;
        private Long elapsed;

        public Long getElapsed() {
            return this.end.toEpochMilli() - this.start.toEpochMilli();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Mall {
        private String mallCode;
        private String mallName;
        private String residueSplitter;
        private String optionStructure;
        private String optionRegex;
        private String optionSplitter;
        private String nameValueRegex;
        private String nameValueSplitter;
    }
}

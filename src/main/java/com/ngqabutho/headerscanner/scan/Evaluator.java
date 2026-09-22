package com.ngqabutho.headerscanner.scan;

import com.ngqabutho.headerscanner.model.HeaderFinding;
import com.ngqabutho.headerscanner.model.HeaderRule;
import com.ngqabutho.headerscanner.model.Status;

import java.net.http.HttpHeaders;
import java.util.Optional;

public class Evaluator {
    public HeaderFinding evaluateHeader(HeaderRule rule, HttpHeaders responseHeaders){
        Optional<String> actualValue = responseHeaders.firstValue(rule.header());

        if (actualValue.isEmpty()){
            return new HeaderFinding(
                    rule,
                    Status.MISSING,
                    null,
                    "Header " + rule.header() + " is not set"
                    );
        }

        String value = actualValue.get();

        if (rule.mustMatch() == null){
            return new HeaderFinding(
                    rule,
                    Status.OK,
                    value,
                    "Present"
            );
        }

        if (rule.mustMatch().matcher(value).find()){
            return new HeaderFinding(
                    rule,
                    Status.OK,
                    value,
                    "Present and matches '" + rule.mustMatch().pattern() + "'"
            );
        }

        return new HeaderFinding(
                rule,
                Status.WEAK,
                value,
                "Present but does not match '" + rule.mustMatch().pattern() + "' (got '" + value + "')"
        );

    }
}

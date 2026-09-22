package com.ngqabutho.headerscanner.rules;

import com.ngqabutho.headerscanner.model.HeaderRule;
import com.ngqabutho.headerscanner.model.Severity;

import java.util.List;
import java.util.regex.Pattern;

public class RuleSet {
    public static final List<HeaderRule> RULES = List.of(
            new HeaderRule(
                    "Strict-Transport-Security",
                    Severity.HIGH,
                    "Enforces HTTPS connections by telling browsers to only use HTTPS.",
                    "Set a positive max-age value and configure HSTS for the site.",
                    Pattern.compile("max-age\\s*=\\s*[1-9]")
                    ),
            new HeaderRule(
                    "Content-Security-Policy",
                    Severity.HIGH,
                    "Controls which sources of content a browser is allowed to load, helping prevent attacks such as cross-site scripting (XSS).",
                    "Set a Content-Security-Policy that restricts content sources to trusted origins.",
                    null
            ),
            new HeaderRule(
                    "X-Content-Type-Options",
                    Severity.MEDIUM,
                    "Prevents browsers from guessing the MIME type of a resource, reducing the risk of content-type confusion attacks.",
                    "Set the header to `nosniff` to prevent browsers from MIME-sniffing responses.",
                    Pattern.compile("nosniff")
            ),
            new HeaderRule(
                    "X-Frame-Options",
                    Severity.MEDIUM,
                    "Controls whether a page can be displayed inside a frame, helping protect against clickjacking attacks.",
                    "Set X-Frame-Options to DENY or SAMEORIGIN to control which sites can frame the page.",
                    null
            ),
            new HeaderRule(
                    "Referrer-Policy",
                    Severity.LOW,
                    "Limits how much of the current URL leaks to other sites when the user clicks an outbound link",
                    "Add Referrer-Policy: strict-origin-when-cross-origin",
                    null
            ),
            new HeaderRule(
                    "Permissions-Policy",
                    Severity.LOW,
                    "Disables browser features the page does not use (camera, microphone, geolocation, payments, etc.)",
                    "Add Permissions-Policy: camera=(), microphone=(), geolocation=()",
                    null
            )
    );
}

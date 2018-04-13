@LoginProfile
Feature: Login wp-admin for current environment

  Scenario: User navigates to current wp-admin page, and using correct credentials to log-in
    Given User getting wp-admin using wpmudev1-php5point4 configuration
    When User is logged as incsubqa
    Then User should see 'PHP 5.4 Single' in top left corner
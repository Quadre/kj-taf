@LoginProfile
Feature: Big Failing scenario

  Background: User navigates to Company home page
    Given User getting wp-admin using wpmudev1-php5point4 configuration
    Then User is logged as incsubqa

#  Scenario: Upload defender plugin from a file system
#    When User opens "Plugins->Add New" page
#    And User installs "defender-security.1.7.6.zip" using Upload Plugin function
#    Then User will observe following lines:
#    | Unpacking the package |
#    | Installing the plugin |
#    | Plugin installed successfully. |

  Scenario: Activate Defender plugins
    When User opens "Plugins->Installed Plugins" page
    And User activates "Defender" plugin version 1.7.6
    #Then Check#1


Feature: Send Event
  Scenario: Event created in event store
    Given empty post topic
    When command to create post is executed
    Then message key should have value as event id

  Scenario Outline: Event with header created in event store
    When command to <type> post is executed
    Then header with name schema should have value <schema>

    Examples:
      |  type  |         schema          |
      | create |  blog_post_created.avsc |
      | update |  blog_post_updated.avsc |
      | delete |  blog_post_deleted.avsc |

  Scenario: Events with same key maintain ordering in event store
    When command to create, update, delete post is executed
    Then order of events should have header schema in order blog_post_created.avsc, blog_post_updated.avsc, blog_post_deleted.avsc

  Scenario: Events created in event store
    Given empty post topic
    When command to create post is executed 1 times
    Then the number of messages in topic should be 1

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CompanyTest.class,
        UnitTest.class,
        RoleTest.class,
        EmployeeTest.class,
        Employee2UnitTest.class
})

public class TestSuite {
}

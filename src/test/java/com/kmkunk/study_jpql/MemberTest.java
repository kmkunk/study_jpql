package com.kmkunk.study_jpql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {
    @Autowired EntityManager em;

    @Test
    public void memberTest() {
        Member member = new Member();
        member.setUsername("member1");
        member.setAge(10);
        em.persist(member);

        List<MemberDto> result =
                em.createQuery("select new com.kmkunk.study_jpql.MemberDto(m.username, m.age) from Member m",
                                MemberDto.class)
                        .getResultList();

        MemberDto memberDto = result.get(0);
        System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
        System.out.println("memberDto.getAge() = " + memberDto.getAge());
    }

    @Test
    public void memberPagingTest() {
        for (int i=0; i<100; i++) {
            Member member = new Member();
            member.setUsername("member" + (i + 1));
            member.setAge(i + 1);
            em.persist(member);
        }

        List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                .setFirstResult(1)
                .setMaxResults(10)
                .getResultList();

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
}
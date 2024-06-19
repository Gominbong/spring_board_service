package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.myproject.domain.QComment.comment;
import static com.example.myproject.domain.QMember.member;
import static com.example.myproject.domain.QMusicList.musicList;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentListQueryDsl(Long musicListId) {

        QMember parentMember = new QMember("parentMember");
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .fetchJoin()
                .innerJoin(comment.member, member)
                .fetchJoin()
                .innerJoin(comment.parentMember, parentMember)
                .fetchJoin()
                .where(musicList.id.eq(musicListId))
                .orderBy(comment.parent.asc(), comment.child1.asc(),
                        comment.child2.asc(), comment.child3.asc(), comment.child4.asc())
                .fetch();
    }

    @Override
    public List<Comment> findByMusicListIdAndDivWidthSizeQueryDsl(Long musicListId, int divWidthSize) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .fetchJoin()
                .where(musicList.id.eq(musicListId), comment.divWidthSize.eq(divWidthSize))
                .fetch();
    }

    @Override
    public Comment findCommentIdQueryDsl(Long commentId) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .fetchJoin()
                .innerJoin(comment.member, member)
                .fetchJoin()
                .where(comment.id.eq(commentId))
                .fetchOne();
    }

    @Override
    public List<Comment> findParentQueryDsl(Long musicListId, int parentId) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .fetchJoin()
                .where(musicList.id.eq(musicListId), comment.parent.eq(parentId))
                .fetch();
    }

    @Override
    public List<Comment> findChild1QueryDsl(Long musicListId, int child1) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .fetchJoin()
                .where(musicList.id.eq(musicListId), comment.child1.eq(child1))
                .fetch();
    }

    @Override
    public List<Comment> findChild2QueryDsl(Long musicListId, int child2) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .fetchJoin()
                .where(musicList.id.eq(musicListId), comment.child2.eq(child2))
                .fetch();
    }

    @Override
    public List<Comment> findChild3QueryDsl(Long musicListId, int child3) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .fetchJoin()
                .where(musicList.id.eq(musicListId), comment.child3.eq(child3))
                .fetch();
    }

    @Override
    public List<Comment> findChild4QueryDsl(Long musicListId, int child4) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .fetchJoin()
                .where(musicList.id.eq(musicListId), comment.child3.eq(child4))
                .fetch();
    }


}

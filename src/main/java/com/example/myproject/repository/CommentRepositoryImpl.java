package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.MusicList;
import com.example.myproject.domain.QMember;
import com.example.myproject.domain.QMusicList;
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
                .orderBy(comment.parent.asc(), comment.child.asc(), comment.child1.asc(),
                        comment.child2.asc(),comment.child3.asc())
                .fetch();
    }

    @Override
    public List<Comment> findByMusicListIdAndDivWidthSizeQueryDsl(Long musicListId, int divWidthSize) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .where(musicList.id.eq(musicListId), comment.parent.eq(divWidthSize))
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
    public List<Comment> findByParentQueryDsl(MusicList musicList, int parent) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, QMusicList.musicList)
                .fetchJoin()
                .innerJoin(comment.member, member)
                .fetchJoin()
                .where(comment.musicList.eq(musicList), comment.parent.eq(parent))
                .fetch();
    }

    @Override
    public List<Comment> findByParentAndChildQueryDsl(MusicList musicList, int parent, int child) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, QMusicList.musicList)
                .fetchJoin()
                .innerJoin(comment.member, member)
                .fetchJoin()
                .where(QMusicList.musicList.eq(musicList), comment.parent.eq(parent), comment.child.eq(child))
                .fetch();
    }

    @Override
    public List<Comment> findByParentAndChildAndChild1QueryDsl(MusicList musicList, int parent, int child, int child1) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, QMusicList.musicList)
                .fetchJoin()
                .innerJoin(comment.member, member)
                .fetchJoin()
                .where(QMusicList.musicList.eq(musicList), comment.parent.eq(parent),
                        comment.child.eq(child), comment.child1.eq(child1))
                .fetch();
    }

    @Override
    public List<Comment> findByParentAndChildAndChild1AndChild2QueryDsl(MusicList musicList, int parent, int child, int child1, int child2) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, QMusicList.musicList)
                .fetchJoin()
                .innerJoin(comment.member, member)
                .fetchJoin()
                .where(QMusicList.musicList.eq(musicList), comment.parent.eq(parent),
                        comment.child.eq(child), comment.child1.eq(child1), comment.child2.eq(child2))
                .fetch();
    }
}

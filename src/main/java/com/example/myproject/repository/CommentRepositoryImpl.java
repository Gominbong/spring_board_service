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
                .where(musicList.id.eq(musicListId))

                .fetch();
    }

    @Override
    public List<Comment> findByMusicListIdAndDepthQueryDsl(Long musicListId,int depth) {
        return queryFactory
                .selectFrom(comment)
                .innerJoin(comment.musicList, musicList)
                .where(musicList.id.eq(musicListId))
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

}

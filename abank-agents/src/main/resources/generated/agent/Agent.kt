/**
 * 
 */
package com.agencybanking.agents

import javax.persistence.Entity
import javax.persistence.Column
import javax.validation.constraints.Size
import javax.persistence.Table
import com.agencybanking.core.data.Active
import javax.validation.constraints.NotBlank
import com.agencybanking.core.el.Label
import com.querydsl.core.BooleanBuilder
import com.agencybanking.core.data.SearchRequest
import com.agencybanking.core.data.BaseEntity
import org.hibernate.annotations.Where

/**
 * @author dubic 
 *
 */
@Active
@Label("Agent")
@Where(clause = "del_time IS NULL")

@Entity
@Table(name = "ag_agent")
class Agent (

        @Label("First Name")
		@NotBlank(message="agents.agent.firstname.empty")
		@Size(min=1,message="agents.agent.firstname.size",max=50)
        @Column(name="first_nm",nullable=false)
        var firstName: String,

        @Label("Last Name")
		@Size(message="agents.agent.lastname.size",min=1,max=50)
		@NotBlank(message="agents.agent.lastname.empty")
        @Column(name="last_nm",nullable=false)
        var lastName: String,

        @Label("Phone")
		@NotBlank(message="agents.agent.phone.empty")
		@Size(message="agents.agent.phone.size",max=50)
        @Column(name="phone",nullable=false)
        var phone: String

    ): BaseEntity() {
    fun predicates() :BooleanBuilder {
        QAgent qAgent = QAgent.agent;
        BooleanBuilder builder = new BooleanBuilder();
        if(!isEmpty(this.getFirstName())){
            builder.and(qAgent.firstName.containsIgnoreCase(this.getFirstName()));
        }
        if(!isEmpty(this.getLastName())){
            builder.and(qAgent.lastName.containsIgnoreCase(this.getLastName()));
        }
        if(!isEmpty(this.getPhone())){
            builder.and(qAgent.phone.containsIgnoreCase(this.getPhone()));
        }
        return builder;
    }
    /**creates predicates with Or Boolean expression
     * @param searchRequest term to search all fields
     * @return or predicates for all fields
     */
    public BooleanBuilder anyPredicates(SearchRequest searchRequest) {
        QAgent qAgent = QAgent.agent;
        BooleanBuilder builder = new BooleanBuilder();
        if(!isEmpty(searchRequest.getSearchItem())){
            builder.or(qAgent.firstName.containsIgnoreCase(searchRequest.getSearchItem()));
            builder.or(qAgent.lastName.containsIgnoreCase(searchRequest.getSearchItem()));
            builder.or(qAgent.phone.containsIgnoreCase(searchRequest.getSearchItem()));
        }
        return builder;
    }


    override fun product(): String {
        return "agent"
    }

    override fun module(): String {
        return AgentsModule.CODE
    }
}